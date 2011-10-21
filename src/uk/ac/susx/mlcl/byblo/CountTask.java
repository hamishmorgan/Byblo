/*
 * Copyright (c) 2010-2011, University of Sussex
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 *  * Neither the name of the University of Sussex nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package uk.ac.susx.mlcl.byblo;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import uk.ac.susx.mlcl.byblo.io.FeatureRecord;
import uk.ac.susx.mlcl.byblo.io.FeatureSink;
import uk.ac.susx.mlcl.byblo.io.EntryRecord;
import uk.ac.susx.mlcl.byblo.io.EntrySink;
import uk.ac.susx.mlcl.byblo.io.EntryFeatureRecord;
import uk.ac.susx.mlcl.byblo.io.EntryFeatureSource;
import uk.ac.susx.mlcl.lib.Checks;
import uk.ac.susx.mlcl.lib.MiscUtil;
import uk.ac.susx.mlcl.lib.ObjectIndex;
import uk.ac.susx.mlcl.lib.io.IOUtil;
import uk.ac.susx.mlcl.lib.tasks.AbstractTask;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.susx.mlcl.byblo.io.SingletonRecordException;
import uk.ac.susx.mlcl.byblo.io.WeightedEntryFeatureRecord;
import uk.ac.susx.mlcl.byblo.io.WeightedEntryFeatureSink;

/**
 * <p>Read in a raw feature instances file, to produce three frequency files:
 * entries, features, and entry-feature pairs.</p>
 *
 * @author Hamish Morgan &lt;hamish.morgan@sussex.ac.uk%gt;
 */
@Parameters(
commandDescription = "Read in a raw feature instances file, to produce three "
        + "frequency files: entries, contexts, and features.")
public class CountTask extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(CountTask.class);

    /**
     * Number of records to read or write between progress updates.
     */
    private static final int PROGRESS_INTERVAL = 1000000;

    @Parameter(names = {"-i", "--input"},
               required = true,
               description = "Source instances file")
    private File instancesFile;

    @Parameter(names = {"-oef", "--output-entry-features"},
               required = true,
               description = "Entry-feature-pair frequencies destination file")
    private File entryFeaturesFile = null;

    @Parameter(names = {"-oe", "--output-entries"},
               required = true,
               description = "Entry frequencies destination file")
    private File entriesFile = null;

    @Parameter(names = {"-of", "--output-features"},
               required = true,
               description = "Feature frequencies destination file.")
    private File featuresFile = null;

    @Parameter(names = {"-c", "--charset"},
               description = "Character encoding to use for input and output.")
    private Charset charset = IOUtil.DEFAULT_CHARSET;

    /**
     * Dependency injection constructor with all fields parameterised.
     *
     * @param instancesFile input file containing entry/context instances
     * @param entryFeaturesFile  output file for entry/context/frequency triples
     * @param entriesFile     output file for entry/frequency pairs
     * @param featuresFile  output file for context/frequency pairs
     * @param charset       character set to use for all file I/O
     * @throws NullPointerException if any argument is null
     */
    public CountTask(final File instancesFile, final File entryFeaturesFile,
            final File entriesFile, final File featuresFile,
            final Charset charset) throws NullPointerException {
        this(instancesFile, entryFeaturesFile, entriesFile, featuresFile);
        setCharset(charset);
    }

    /**
     * Minimal parameterisation constructor, with all fields that must be set
     * for the task to be functional. Character set will be set to software
     * default from {@link IOUtil#DEFAULT_CHARSET}.
     *
     * @param instancesFile input file containing entry/context instances
     * @param entryFeaturesFile  output file for entry/context/frequency triples
     * @param entriesFile     output file for entry/frequency pairs
     * @param featuresFile  output file for context/frequency pairs
     * @throws NullPointerException if any argument is null
     */
    public CountTask(
            final File instancesFile, final File entryFeaturesFile,
            final File entriesFile, final File featuresFile)
            throws NullPointerException {
        setInstancesFile(instancesFile);
        setEntryFeaturesFile(entryFeaturesFile);
        setEntriesFile(entriesFile);
        setFeaturesFile(featuresFile);
    }

    /**
     * Default constructor used by serialisation and JCommander instantiation.
     * All files will initially be set to null. Character set will be set to
     * software default from {@link IOUtil#DEFAULT_CHARSET}.
     */
    public CountTask() {
    }

    @Override
    protected void initialiseTask() throws Exception {
        checkState();
    }

    @Override
    protected void runTask() throws Exception {
        if (LOG.isInfoEnabled())
            LOG.info("Running " + this + ".");

        {
            final ObjectIndex<String> entryIndex = new ObjectIndex<String>();

            final ObjectIndex<String> featureIndex = new ObjectIndex<String>();

            final Object2IntMap<EntryFeatureRecord> entryFeatureFreq =
                    new Object2IntOpenHashMap<EntryFeatureRecord>();
            entryFeatureFreq.defaultReturnValue(0);

            {
                final Int2IntMap featureFreq = new Int2IntOpenHashMap();
                featureFreq.defaultReturnValue(0);

                {
                    final Int2IntMap entryFreq = new Int2IntOpenHashMap();
                    entryFreq.defaultReturnValue(0);

                    countInstances(entryFreq, featureFreq,
                            entryFeatureFreq, entryIndex, featureIndex);

                    writeEntries(entryFreq, entryIndex);
                }

                writeContexts(featureFreq, featureIndex);
            }


            writeFeatures(entryFeatureFreq, entryIndex, featureIndex);

        }

        if (LOG.isInfoEnabled())
            LOG.info("Completed " + this + ".");
    }

    @Override
    protected void finaliseTask() throws Exception {
    }

    private void countInstances(
            final Int2IntMap entryFreq,
            final Int2IntMap featureFreq,
            final Object2IntMap<? super EntryFeatureRecord> entryFeatureFreq,
            final ObjectIndex<String> entryIndex,
            final ObjectIndex<String> featureIndex)
            throws IOException {

        if (LOG.isInfoEnabled())
            LOG.info(
                    "Reading entry/context instances from " + instancesFile + ".");
        final EntryFeatureSource instanceSource =
                new EntryFeatureSource(instancesFile, charset, entryIndex,
                featureIndex);

        if (!instanceSource.hasNext() && LOG.isWarnEnabled())
            LOG.warn("Instances file is empty.");

        long i = 0;
        while (instanceSource.hasNext()) {
            final EntryFeatureRecord instance;
            try {
                instance = instanceSource.read();
            } catch (SingletonRecordException ex) {
                LOG.warn("Badly formed input data: " + ex.getMessage());
                continue;
            }
            
            final int entry_id = instance.getEntryId();
            final int feature_id = instance.getFeatureId();

            entryFreq.put(entry_id, entryFreq.get(entry_id) + 1);
            featureFreq.put(feature_id, featureFreq.get(feature_id) + 1);
            entryFeatureFreq.put(instance, entryFeatureFreq.getInt(instance) + 1);

            if ((++i % PROGRESS_INTERVAL == 0 || !instanceSource.hasNext())
                    && LOG.isInfoEnabled()) {
                LOG.info("Read " + i + " instances. Found "
                        + entryFreq.size() + " entries, " + featureFreq.size()
                        + " features, and " + entryFeatureFreq.size()
                        + " entry-features. (" + (int) instanceSource.
                        percentRead()
                        + "% complete)");
                LOG.debug(MiscUtil.memoryInfoString());
            }
        }
    }

    private void writeEntries(
            final Int2IntMap entryFreq,
            final ObjectIndex<String> entryIndex)
            throws IOException {

        LOG.info("Sorting entries frequency data.");
        LOG.debug(MiscUtil.memoryInfoString());

        List<Int2IntMap.Entry> entryFreqList =
                new ArrayList<Int2IntMap.Entry>(entryFreq.int2IntEntrySet());
        Collections.sort(entryFreqList, new Comparator<Int2IntMap.Entry>() {

            @Override
            public int compare(Entry o1, Entry o2) {
                return entryIndex.get(o1.getIntKey()).compareTo(
                        entryIndex.get(o2.getIntKey()));
            }
        });


        LOG.info("Writing entries frequency data to file " + entriesFile + ".");
        LOG.debug(MiscUtil.memoryInfoString());

        if (entriesFile.exists() && LOG.isWarnEnabled())
            LOG.warn("The entries file already exists and will be overwritten.");

        EntrySink entriesink = null;
        final int n = entryFreqList.size();
        try {
            entriesink = new EntrySink(entriesFile, charset, entryIndex);
            int i = 0;
            for (final Int2IntMap.Entry entry : entryFreqList) {
                entriesink.write(new EntryRecord(
                        entry.getIntKey(),
                        //                        entry.getIntKey(),
                        //                        entryUniqueContexts.get(entry.getIntKey()).size(),
                        entry.getIntValue()));
                if ((++i % PROGRESS_INTERVAL == 0 || i == n) && LOG.
                        isInfoEnabled())
                    LOG.info("Wrote " + i + "/" + n + " entries. ("
                            + (int) (i * 100d / n) + "% complete)");
            }
        } finally {
            if (entriesink != null) {
                entriesink.flush();
                entriesink.close();
            }
        }
    }

    private void writeContexts(
            final Int2IntMap featureFreq,
            final ObjectIndex<String> featureIdex)
            throws IOException {

        LOG.info("Sorting context frequency data.");
        LOG.debug(MiscUtil.memoryInfoString());

        List<Int2IntMap.Entry> contextFreqList =
                new ArrayList<Int2IntMap.Entry>(featureFreq.int2IntEntrySet());
        Collections.sort(contextFreqList, new Comparator<Int2IntMap.Entry>() {

            @Override
            public int compare(Entry o1, Entry o2) {
                return featureIdex.get(o1.getIntKey()).compareTo(
                        featureIdex.get(o2.getIntKey()));
            }
        });

        if (LOG.isInfoEnabled())
            LOG.info("Writing context frequency data to " + featuresFile + ".");
        LOG.debug(MiscUtil.memoryInfoString());

        if (featuresFile.exists() && LOG.isWarnEnabled())
            LOG.warn("The contexts file already exists and will be overwritten.");

        FeatureSink contextSink = null;
        final int n = contextFreqList.size();
        try {
            contextSink = new FeatureSink(featuresFile, charset,
                    featureIdex);
            int i = 0;
            for (final Int2IntMap.Entry context : contextFreqList) {
                contextSink.write(new FeatureRecord(
                        context.getIntKey(),
                        context.getIntValue()));
                if ((++i % PROGRESS_INTERVAL == 0 || i == n) && LOG.
                        isInfoEnabled())
                    LOG.info("Wrote " + i + "/" + n + " contexts. ("
                            + (int) (i * 100d / n) + "% complete)");
            }
        } finally {
            if (contextSink != null) {
                contextSink.flush();
                contextSink.close();
            }
        }
    }

    private void writeFeatures(
            final Object2IntMap<? extends EntryFeatureRecord> entryFeatureFreq,
            final ObjectIndex<String> entryIndex,
            final ObjectIndex<String> featureIndex)
            throws FileNotFoundException, IOException {

        LOG.info("Sorting feature pairs frequency data.");

        List<Object2IntMap.Entry<? extends EntryFeatureRecord>> contextFreqList =
                new ArrayList<Object2IntMap.Entry<? extends EntryFeatureRecord>>(entryFeatureFreq.
                object2IntEntrySet());
        Collections.sort(contextFreqList,
                new Comparator<Object2IntMap.Entry<? extends EntryFeatureRecord>>() {

                    @Override
                    public int compare(
                            Object2IntMap.Entry<? extends EntryFeatureRecord> o1,
                            Object2IntMap.Entry<? extends EntryFeatureRecord> o2) {
                        int v = entryIndex.get(o1.getKey().getEntryId()).
                                compareTo(
                                entryIndex.get(o2.getKey().getEntryId()));
                        if (v == 0) {
                            v = featureIndex.get(o1.getKey().getFeatureId()).
                                    compareTo(
                                    featureIndex.get(o2.getKey().getFeatureId()));
                        }
                        return v;
                    }
                });

        if (LOG.isInfoEnabled())
            LOG.info(
                    "Writing feature pairs frequency data to file  " + entryFeaturesFile + ".");
        LOG.debug(MiscUtil.memoryInfoString());

        if (entryFeaturesFile.exists() && LOG.isWarnEnabled())
            LOG.warn("The features file already exists and will be overwritten.");

        WeightedEntryFeatureSink featureSink = null;
        final int n = contextFreqList.size();
        try {
            featureSink = new WeightedEntryFeatureSink(entryFeaturesFile,
                    charset, entryIndex,
                    featureIndex);
            int i = 0;
            for (final Object2IntMap.Entry<? extends EntryFeatureRecord> feature :
                    contextFreqList) {
                featureSink.write(new WeightedEntryFeatureRecord(
                        feature.getKey().getEntryId(),
                        feature.getKey().getFeatureId(),
                        feature.getIntValue()));
                if ((++i % PROGRESS_INTERVAL == 0 || i == n) && LOG.
                        isInfoEnabled())
                    LOG.info("Wrote " + i + "/" + n + " features. ("
                            + (int) (i * 100d / n) + "% complete)");
            }
        } finally {
            if (featureSink != null) {
                featureSink.flush();
                featureSink.close();
            }
        }
    }

    public final File getFeaturesFile() {
        return featuresFile;
    }

    public final void setFeaturesFile(final File featuresFile)
            throws NullPointerException {
        if (featuresFile == null)
            throw new NullPointerException("featuresFile is null");
        this.featuresFile = featuresFile;
    }

    public final File getEntryFeaturesFile() {
        return entryFeaturesFile;
    }

    public final void setEntryFeaturesFile(final File entryFeaturesFile)
            throws NullPointerException {
        if (entryFeaturesFile == null)
            throw new NullPointerException("entryFeaturesFile is null");
        this.entryFeaturesFile = entryFeaturesFile;
    }

    public final File getEntriesFile() {
        return entriesFile;
    }

    public final void setEntriesFile(final File entriesFile)
            throws NullPointerException {
        if (entriesFile == null)
            throw new NullPointerException("entriesFile is null");
        this.entriesFile = entriesFile;
    }

    public File getInputFile() {
        return instancesFile;
    }

    public final void setInstancesFile(final File inputFile)
            throws NullPointerException {
        if (inputFile == null)
            throw new NullPointerException("inputFile is null");
        this.instancesFile = inputFile;
    }

    public final Charset getCharset() {
        return charset;
    }

    public final void setCharset(Charset charset) {
        Checks.checkNotNull(charset);
        this.charset = charset;
    }

    /**
     * Method that performance a number of sanity checks on the parameterisation
     * of this class. It is necessary to do this because the the class can be
     * instantiated via a null constructor when run from the command line.
     *
     * @throws NullPointerException
     * @throws IllegalStateException
     * @throws FileNotFoundException
     */
    private void checkState() throws NullPointerException, IllegalStateException, FileNotFoundException {
        // Check non of the parameters are null
        if (instancesFile == null)
            throw new NullPointerException("inputFile is null");
        if (entryFeaturesFile == null)
            throw new NullPointerException("entryFeaturesFile is null");
        if (featuresFile == null)
            throw new NullPointerException("featuresFile is null");
        if (entriesFile == null)
            throw new NullPointerException("entriesFile is null");
        if (charset == null)
            throw new NullPointerException("charset is null");

        // Check that no two files are the same
        if (instancesFile.equals(entryFeaturesFile))
            throw new IllegalStateException("inputFile == featuresFile");
        if (instancesFile.equals(featuresFile))
            throw new IllegalStateException("inputFile == contextsFile");
        if (instancesFile.equals(entriesFile))
            throw new IllegalStateException("inputFile == entriesFile");
        if (entryFeaturesFile.equals(featuresFile))
            throw new IllegalStateException("entryFeaturesFile == featuresFile");
        if (entryFeaturesFile.equals(entriesFile))
            throw new IllegalStateException("entryFeaturesFile == entriesFile");
        if (featuresFile.equals(entriesFile))
            throw new IllegalStateException("featuresFile == entriesFile");


        // Check that the instances file exists and is readable
        if (!instancesFile.exists())
            throw new FileNotFoundException(
                    "instances file does not exist: " + instancesFile);
        if (!instancesFile.isFile())
            throw new IllegalStateException(
                    "instances file is not a normal data file: " + instancesFile);
        if (!instancesFile.canRead())
            throw new IllegalStateException(
                    "instances file is not readable: " + instancesFile);

        // For each output file, check that either it exists and it writeable,
        // or that it does not exist but is creatable
        if (entriesFile.exists() && (!entriesFile.isFile() || !entriesFile.
                canWrite()))
            throw new IllegalStateException(
                    "entries file exists but is not writable: " + entriesFile);
        if (!entriesFile.exists() && !entriesFile.getAbsoluteFile().
                getParentFile().
                canWrite()) {
            throw new IllegalStateException(
                    "entries file does not exists and can not be reated: " + entriesFile);
        }
        if (featuresFile.exists() && (!featuresFile.isFile() || !featuresFile.
                canWrite()))
            throw new IllegalStateException(
                    "features file exists but is not writable: " + featuresFile);
        if (!featuresFile.exists() && !featuresFile.getAbsoluteFile().
                getParentFile().
                canWrite()) {
            throw new IllegalStateException(
                    "features file does not exists and can not be reated: " + featuresFile);
        }
        if (entryFeaturesFile.exists() && (!entryFeaturesFile.isFile() || !entryFeaturesFile.
                canWrite()))
            throw new IllegalStateException(
                    "entry-features file exists but is not writable: " + entryFeaturesFile);
        if (!entryFeaturesFile.exists() && !entryFeaturesFile.getAbsoluteFile().
                getParentFile().
                canWrite()) {
            throw new IllegalStateException(
                    "entry-features file does not exists and can not be reated: " + entryFeaturesFile);
        }
    }

    @Override
    public String toString() {
        return "CountTask{"
                + "instancesFile=" + instancesFile
                + ", entryFeaturesFile=" + entryFeaturesFile
                + ", entriesFile=" + entriesFile
                + ", featuresFile=" + featuresFile
                + ", charset=" + charset
                + '}';
    }
}