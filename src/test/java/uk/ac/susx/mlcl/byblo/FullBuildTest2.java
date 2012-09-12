/*
 * Copyright (c) 2010-2012, University of Sussex
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

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.ac.susx.mlcl.TestConstants;
import uk.ac.susx.mlcl.byblo.commands.*;
import uk.ac.susx.mlcl.byblo.enumerators.DoubleEnumeratingDelegate;
import uk.ac.susx.mlcl.byblo.enumerators.EnumeratingDelegates;
import uk.ac.susx.mlcl.byblo.enumerators.EnumeratorType;
import uk.ac.susx.mlcl.byblo.enumerators.SingleEnumeratingDelegate;
import uk.ac.susx.mlcl.lib.io.TempFileFactory;
import uk.ac.susx.mlcl.testing.SlowTestCategory;

import java.io.File;
import java.nio.charset.Charset;

import static uk.ac.susx.mlcl.TestConstants.*;

/**
 * @author Hamish I A Morgan &lt;hamish.morgan@sussex.ac.uk&gt;
 */
public class FullBuildTest2 {

    // @Test
    // public void serialBuildTest() throws Exception {
    // System.out.println("Testing Full Build (serial)");
    // final String affix = "sb.";
    //
    // final File instances = TEST_FRUIT_INPUT;
    // final Charset charet = DEFAULT_CHARSET;
    // boolean preindexedEntries = false;
    // boolean preindexedFeatures = false;
    //
    // File events = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".events");
    // File entries = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entries");
    // File features = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".features");
    //
    // File eventsFiltered = new File(TEST_OUTPUT_DIR,
    // events.getName() + ".filtered");
    // File entriesFiltered = new File(TEST_OUTPUT_DIR,
    // entries.getName() + ".filtered");
    // File featuresFiltered = new File(TEST_OUTPUT_DIR,
    // features.getName() + ".filtered");
    //
    // File similarities = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".sims");
    //
    // File neighbours = new File(TEST_OUTPUT_DIR,
    // similarities.getName() + ".neighs");
    //
    // // Count the entries, features and events
    //
    // assertValidPlaintextInputFiles(instances);
    // deleteIfExist(events, entries, features);
    //
    // CountCommand count = new CountCommand(
    // instances, events, entries, features,
    // new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false),
    // charset);
    // count.runCommand();
    //
    // // Filter
    //
    // assertValidPlaintextInputFiles(entries, features, events);
    // deleteIfExist(eventsFiltered, entriesFiltered, featuresFiltered);
    //
    // FilterCommand filter = new FilterCommand(
    // events, entries, features,
    // eventsFiltered, entriesFiltered, featuresFiltered, charset);
    // filter.addEventMinimumFrequency(2);
    // filter.runCommand();
    //
    // assertTrue("Filtered events file is no smaller that events file.",
    // events.length() > eventsFiltered.length());
    // assertTrue("Filtered entries file is no smaller that entries file.",
    // entries.length() > entriesFiltered.length());
    // assertTrue("Filtered features file is no smaller that features file.",
    // features.length() > featuresFiltered.length());
    //
    // // All pairs
    //
    // assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
    // featuresFiltered);
    // deleteIfExist(similarities);
    //
    // AllPairsCommand allpairs = new AllPairsCommand(
    // entriesFiltered, featuresFiltered, eventsFiltered, similarities,
    // charet, new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false));
    // allpairs.setnThreads(1);
    // allpairs.runCommand();
    //
    // // KNN
    // assertValidPlaintextInputFiles(similarities);
    // deleteIfExist(neighbours);
    //
    // KnnSimsCommand knn = new KnnSimsCommand(
    // similarities, neighbours, charet,
    // new SingleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, null, false, false), 5);
    // knn.runCommand();
    //
    // assertValidPlaintextInputFiles(neighbours);
    // assertTrue("Neighbours file is no smaller that similarities file.",
    // neighbours.length() < similarities.length());
    //
    // }
    //
    // @Test
    // public void parallelBuildTest() throws Exception {
    // System.out.println("Testing Full Build (parallel)");
    // final String affix = "pb.";
    //
    // final File instances = TEST_FRUIT_INPUT;
    // final Charset charet = DEFAULT_CHARSET;
    // boolean preindexedEntries = false;
    // boolean preindexedFeatures = false;
    //
    // File events = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".events");
    // File entries = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entries");
    // File features = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".features");
    //
    // File eventsFiltered = new File(TEST_OUTPUT_DIR,
    // events.getName() + ".filtered");
    // File entriesFiltered = new File(TEST_OUTPUT_DIR,
    // entries.getName() + ".filtered");
    // File featuresFiltered = new File(TEST_OUTPUT_DIR,
    // features.getName() + ".filtered");
    //
    // File similarities = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".sims");
    //
    // File neighbours = new File(TEST_OUTPUT_DIR,
    // similarities.getName() + ".neighs");
    //
    // // Count the entries, features and events
    //
    // assertValidPlaintextInputFiles(instances);
    // deleteIfExist(events, entries, features);
    //
    //
    // ExternalCountCommand count = new ExternalCountCommand(
    // instances, events, entries, features, charet,
    // new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false));
    // count.runCommand();
    //
    // // Filter
    //
    // assertValidPlaintextInputFiles(entries, features, events);
    // deleteIfExist(eventsFiltered, entriesFiltered, featuresFiltered);
    //
    // FilterCommand filter = new FilterCommand(
    // events, entries, features,
    // eventsFiltered, entriesFiltered, featuresFiltered, charet);
    // filter.addEventMinimumFrequency(2);
    // filter.runCommand();
    //
    // assertTrue("Filtered events file is no smaller that events file.",
    // events.length() > eventsFiltered.length());
    // assertTrue("Filtered entries file is no smaller that entries file.",
    // entries.length() > entriesFiltered.length());
    // assertTrue("Filtered features file is no smaller that features file.",
    // features.length() > featuresFiltered.length());
    //
    // // All pairs
    //
    // assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
    // featuresFiltered);
    // deleteIfExist(similarities);
    //
    // AllPairsCommand allpairs = new AllPairsCommand(
    // entriesFiltered, featuresFiltered, eventsFiltered, similarities,
    // charet, new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false));
    // allpairs.runCommand();
    //
    // // KNN
    // assertValidPlaintextInputFiles(similarities);
    // deleteIfExist(neighbours);
    //
    // ExternalKnnSimsCommand knn = new ExternalKnnSimsCommand(
    // similarities, neighbours, charet,
    // new SingleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, null, false, false), 5);
    // knn.runCommand();
    //
    // assertValidPlaintextInputFiles(neighbours);
    // assertTrue("Neighbours file is no smaller that similarities file.",
    // neighbours.length() < similarities.length());
    //
    // }
    //
    // @Test
    // public void serialBuildTest_Indexed() throws Exception {
    // System.out.println("Testing Full Build (serial, preindexed)");
    // final String affix = "sbi.";
    //
    // final File instances = TEST_FRUIT_INPUT;
    // final Charset charet = DEFAULT_CHARSET;
    // boolean preindexedEntries = true;
    // boolean preindexedFeatures = true;
    //
    // File instancesIndexed = new File(TEST_OUTPUT_DIR, affix +
    // instances.getName() + ".indexed");
    // File entryIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entry-index");
    // File featureIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".feature-index");
    //
    // File events = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".events");
    // File entries = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entries");
    // File features = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".features");
    //
    // File eventsFiltered = new File(TEST_OUTPUT_DIR,
    // events.getName() + ".filtered");
    // File entriesFiltered = new File(TEST_OUTPUT_DIR,
    // entries.getName() + ".filtered");
    // File featuresFiltered = new File(TEST_OUTPUT_DIR,
    // features.getName() + ".filtered");
    //
    // File similarities = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".sims");
    //
    // File neighbours = new File(TEST_OUTPUT_DIR,
    // similarities.getName() + ".neighs");
    //
    // File neighboursStrings = new File(TEST_OUTPUT_DIR,
    // neighbours.getName() + ".strings");
    //
    //
    //
    // // Index the strings, reproducing the instances file in indexed form
    //
    // deleteIfExist(entryIndex, featureIndex, instancesIndexed);
    //
    // indexTP(instances, instancesIndexed, entryIndex, featureIndex,
    // Enumerating.DEFAULT_TYPE,
    // false,
    // false, false);
    //
    //
    // // Count the entries, features and events
    //
    // deleteIfExist(events, entries, features);
    //
    // CountCommand count = new CountCommand(
    // instancesIndexed, events, entries, features,
    // new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false),
    // charet);
    // count.runCommand();
    //
    // assertValidPlaintextInputFiles(entries, features, events);
    // assertSizeGT(TEST_FRUIT_ENTRY_FEATURES, events);
    // assertSizeGT(TEST_FRUIT_ENTRIES, entries);
    // assertSizeGT(TEST_FRUIT_FEATURES, features);
    //
    // // Filter
    //
    //
    //
    // deleteIfExist(eventsFiltered, featuresFiltered, entriesFiltered);
    //
    //
    // filter(events, entries, features,
    // eventsFiltered, entriesFiltered, featuresFiltered,
    // entryIndex, featureIndex, preindexedEntries, preindexedFeatures, false,
    // false);
    //
    // assertSizeGT(events, eventsFiltered);
    // assertSizeGT(entries, entriesFiltered);
    // assertSizeGT(features, featuresFiltered);
    //
    // // All pairs
    //
    // assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
    // featuresFiltered);
    // deleteIfExist(similarities);
    //
    // AllPairsCommand allpairs = new AllPairsCommand(
    // entriesFiltered, featuresFiltered, eventsFiltered, similarities,
    // charet, new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false));
    // allpairs.setnThreads(1);
    // allpairs.runCommand();
    //
    // assertValidPlaintextInputFiles(similarities);
    // assertSizeGT(TEST_FRUIT_SIMS, similarities);
    //
    // // KNN
    //
    //
    // deleteIfExist(neighbours);
    //
    // KnnSimsCommand knn = new KnnSimsCommand(
    // similarities, neighbours, charet,
    // new SingleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, null, false, false), 5);
    // knn.runCommand();
    //
    // assertValidPlaintextInputFiles(neighbours);
    // assertSizeGT(similarities, neighbours);
    // assertSizeGT(TEST_FRUIT_SIMS_100NN, neighbours);
    //
    // // Finally, convert neighbours back to strings
    //
    // deleteIfExist(neighboursStrings);
    //
    // unindexSims(neighbours, suffix(neighbours, ".strings"), entryIndex,
    // EnumeratorType.Memory, false, false, false);
    //
    // assertValidPlaintextInputFiles(neighboursStrings);
    // }
    //
    // @Test
    // public void parallelBuildTest_Indexed() throws Exception {
    // System.out.println("Testing Full Build (parallel, preindexed)");
    // final String affix = "pbi.";
    //
    // final File instances = TEST_FRUIT_INPUT;
    // final Charset charet = DEFAULT_CHARSET;
    // boolean preindexedEntries = true;
    // boolean preindexedFeatures = true;
    //
    // File instancesIndexed = new File(TEST_OUTPUT_DIR, affix +
    // instances.getName() + ".indexed");
    // File entryIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entry-index");
    // File featureIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".feature-index");
    //
    // File events = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".events");
    // File entries = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entries");
    // File features = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".features");
    //
    // File eventsFiltered = new File(TEST_OUTPUT_DIR,
    // events.getName() + ".filtered");
    // File entriesFiltered = new File(TEST_OUTPUT_DIR,
    // entries.getName() + ".filtered");
    // File featuresFiltered = new File(TEST_OUTPUT_DIR,
    // features.getName() + ".filtered");
    //
    // File similarities = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".sims");
    //
    // File neighbours = new File(TEST_OUTPUT_DIR,
    // similarities.getName() + ".neighs");
    //
    // File neighboursStrings = new File(TEST_OUTPUT_DIR,
    // neighbours.getName() + ".strings");
    //
    //
    //
    // // Index the strings, reproducing the instances file in indexed form
    //
    // deleteIfExist(entryIndex, featureIndex, instancesIndexed);
    //
    // indexTP(instances, instancesIndexed, entryIndex, featureIndex,
    // Enumerating.DEFAULT_TYPE, false,
    // false, false);
    //
    //
    // // Count the entries, features and events
    //
    // deleteIfExist(events, entries, features);
    //
    // ExternalCountCommand count = new ExternalCountCommand();
    // count.setInstancesFile(instancesIndexed);
    // count.setEntriesFile(entries);
    // count.setFeaturesFile(features);
    // count.setEventsFile(events);
    // count.setIndexDelegate(new
    // DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false));
    // count.getFileDelegate().setCharset(charet);
    // count.runCommand();
    //
    // assertValidPlaintextInputFiles(entries, features, events);
    // assertSizeGT(TEST_FRUIT_ENTRY_FEATURES, events);
    // assertSizeGT(TEST_FRUIT_ENTRIES, entries);
    // assertSizeGT(TEST_FRUIT_FEATURES, features);
    //
    // // Filter
    //
    //
    //
    // deleteIfExist(eventsFiltered, featuresFiltered, entriesFiltered);
    //
    //
    // filter(events, entries, features, eventsFiltered, entriesFiltered,
    // featuresFiltered, entryIndex, featureIndex, preindexedEntries,
    // preindexedFeatures, false, false);
    //
    // // All pairs
    //
    // assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
    // featuresFiltered);
    // deleteIfExist(similarities);
    //
    // AllPairsCommand allpairs = new AllPairsCommand(
    // entriesFiltered, featuresFiltered, eventsFiltered, similarities,
    // charet, new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, false, false));
    // allpairs.runCommand();
    //
    // assertValidPlaintextInputFiles(similarities);
    // assertSizeGT(TEST_FRUIT_SIMS, similarities);
    //
    // // KNN
    //
    //
    // deleteIfExist(neighbours);
    //
    // ExternalKnnSimsCommand knn = new ExternalKnnSimsCommand(
    // similarities, neighbours, charet,
    // new SingleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, null, false, false), 5);
    // knn.runCommand();
    //
    // assertValidPlaintextInputFiles(neighbours);
    // assertSizeGT(similarities, neighbours);
    // assertSizeGT(TEST_FRUIT_SIMS_100NN, neighbours);
    //
    // // Finally, convert neighbours back to strings
    //
    // deleteIfExist(neighboursStrings);
    //
    // unindexSims(neighbours, suffix(neighbours, ".strings"), entryIndex,
    // EnumeratorType.Memory, false, false, false);
    //
    // assertValidPlaintextInputFiles(neighboursStrings);
    // }
    //
    // @Test
    // public void serialBuildTest_SkipIndexed() throws Exception {
    // System.out.println("Testing Full Build (serial, preindexed, skip)");
    // final String affix = "sbsi.";
    //
    // final File instances = TEST_FRUIT_INPUT;
    // final Charset charet = DEFAULT_CHARSET;
    // boolean preindexedEntries = true;
    // boolean preindexedFeatures = true;
    // boolean skipIndex1 = true;
    // boolean skipIndex2 = true;
    // EnumeratorType type = EnumeratorType.Memory;
    //
    // File instancesIndexed = new File(TEST_OUTPUT_DIR, affix +
    // instances.getName() + ".indexed");
    // File entryIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entry-index");
    // File featureIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".feature-index");
    //
    // File events = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".events");
    // File entries = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entries");
    // File features = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".features");
    //
    // File eventsFiltered = new File(TEST_OUTPUT_DIR,
    // events.getName() + ".filtered");
    // File entriesFiltered = new File(TEST_OUTPUT_DIR,
    // entries.getName() + ".filtered");
    // File featuresFiltered = new File(TEST_OUTPUT_DIR,
    // features.getName() + ".filtered");
    //
    // File similarities = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".sims");
    //
    // File neighbours = new File(TEST_OUTPUT_DIR,
    // similarities.getName() + ".neighs");
    //
    // File neighboursStrings = new File(TEST_OUTPUT_DIR,
    // neighbours.getName() + ".strings");
    //
    //
    //
    // // Index the strings, reproducing the instances file in indexed form
    //
    // deleteIfExist(entryIndex, featureIndex, instancesIndexed);
    //
    // indexTP(instances, instancesIndexed, entryIndex, featureIndex,
    // Enumerating.DEFAULT_TYPE,
    // skipIndex1, skipIndex2, false);
    //
    // unindexTP(instancesIndexed, suffix(instancesIndexed, ".strings"),
    // entryIndex, featureIndex,
    // skipIndex1, skipIndex2);
    //
    // // Count the entries, features and events
    //
    // deleteIfExist(events, entries, features);
    //
    // CountCommand count = new CountCommand(
    // instancesIndexed, events, entries, features,
    // new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, skipIndex1, skipIndex2),
    // charet);
    //
    // count.runCommand();
    //
    // assertValidPlaintextInputFiles(entries, features, events);
    // assertSizeGT(TEST_FRUIT_ENTRY_FEATURES, events);
    // assertSizeGT(TEST_FRUIT_ENTRIES, entries);
    // assertSizeGT(TEST_FRUIT_FEATURES, features);
    //
    // unindexWT(entries, suffix(entries, ".strings"), entryIndex,
    // skipIndex1, skipIndex2);
    // unindexWT(features, suffix(features, ".strings"), featureIndex,
    // skipIndex1, skipIndex2);
    // unindexWTP(events, suffix(events, ".strings"), entryIndex, featureIndex,
    // skipIndex1, skipIndex2);
    //
    // // Filter
    //
    //
    //
    // deleteIfExist(eventsFiltered, featuresFiltered, entriesFiltered);
    //
    //
    // filter(events, entries, features, eventsFiltered, entriesFiltered,
    // featuresFiltered, entryIndex, featureIndex, preindexedEntries,
    // preindexedFeatures, skipIndex1, skipIndex2);
    //
    //
    // unindexWT(entriesFiltered, suffix(entriesFiltered, ".strings"),
    // entryIndex, skipIndex1, skipIndex2);
    // unindexWT(featuresFiltered, suffix(featuresFiltered, ".strings"),
    // featureIndex, skipIndex1, skipIndex2);
    // unindexWTP(eventsFiltered, suffix(eventsFiltered, ".strings"),
    // entryIndex, featureIndex, skipIndex1, skipIndex2);
    //
    // // All pairs
    //
    // assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
    // featuresFiltered);
    // deleteIfExist(similarities);
    //
    // AllPairsCommand allpairs = new AllPairsCommand(
    // entriesFiltered, featuresFiltered, eventsFiltered, similarities,
    // charet,
    // new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, skipIndex1, skipIndex2));
    // allpairs.setnThreads(1);
    // allpairs.runCommand();
    //
    // assertValidPlaintextInputFiles(similarities);
    // assertSizeGT(TEST_FRUIT_SIMS, similarities);
    //
    // unindexSims(similarities, suffix(similarities, ".strings"), entryIndex,
    // EnumeratorType.Memory, skipIndex1, skipIndex2, false);
    //
    // // KNN
    //
    //
    // deleteIfExist(neighbours);
    //
    // knn(similarities, neighbours, preindexedEntries,
    // skipIndex1, skipIndex2);
    //
    // // Finally, convert neighbours back to strings
    //
    // deleteIfExist(neighboursStrings);
    //
    // unindexSims(neighbours, suffix(neighbours, ".strings"), entryIndex,
    // EnumeratorType.Memory, skipIndex1, skipIndex2, false);
    //
    // }
    // @Test
    // public void parallelBuildTest_SkipIndexed() throws Exception {
    // System.out.println("Testing Full Build (parallel, preindexed, skip)");
    // final String affix = "x.";
    // boolean serial = true;
    // boolean preindexedEntries = true;
    // boolean preindexedFeatures = true;
    // boolean skipIndex1 = true;
    // boolean skipIndex2 = true;
    // EnumeratorType type = EnumeratorType.Memory;
    //
    // File instancesIndexed = new File(TEST_OUTPUT_DIR, affix +
    // instances.getName() + ".indexed");
    // File entryIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entry-index");
    // File featureIndex = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".feature-index");
    //
    // File events = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".events");
    // File entries = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".entries");
    // File features = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".features");
    //
    // File eventsFiltered = new File(TEST_OUTPUT_DIR,
    // events.getName() + ".filtered");
    // File entriesFiltered = new File(TEST_OUTPUT_DIR,
    // entries.getName() + ".filtered");
    // File featuresFiltered = new File(TEST_OUTPUT_DIR,
    // features.getName() + ".filtered");
    //
    // File similarities = new File(TEST_OUTPUT_DIR,
    // affix + instances.getName() + ".sims");
    //
    // File neighbours = new File(TEST_OUTPUT_DIR,
    // similarities.getName() + ".neighs");
    //
    // File neighboursStrings = new File(TEST_OUTPUT_DIR,
    // neighbours.getName() + ".strings");
    //
    //
    //
    // // Index the strings, reproducing the instances file in indexed form
    //
    // deleteIfExist(entryIndex, featureIndex, instancesIndexed);
    //
    // indexTP(instances, instancesIndexed, entryIndex, featureIndex,
    // Enumerating.DEFAULT_TYPE,
    // skipIndex1, skipIndex2, false);
    //
    // unindexTP(instancesIndexed, suffix(instancesIndexed, ".strings"),
    // entryIndex, featureIndex,
    // skipIndex1, skipIndex2);
    //
    // // Count the entries, features and events
    //
    // deleteIfExist(events, entries, features);
    //
    // ExternalCountCommand count = new ExternalCountCommand();
    // count.setInstancesFile(instancesIndexed);
    // count.setEntriesFile(entries);
    // count.setFeaturesFile(features);
    // count.setEventsFile(events);
    // count.setIndexDelegate(new DoubleEnumeratingDelegate(
    // Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, skipIndex1, skipIndex2));
    // count.getFileDelegate().setCharset(charet);
    // count.runCommand();
    //
    // assertValidPlaintextInputFiles(entries, features, events);
    // assertSizeGT(TEST_FRUIT_ENTRY_FEATURES, events);
    // assertSizeGT(TEST_FRUIT_ENTRIES, entries);
    // assertSizeGT(TEST_FRUIT_FEATURES, features);
    //
    // unindexWT(entries, suffix(entries, ".strings"), entryIndex,
    // skipIndex1, skipIndex2);
    // unindexWT(features, suffix(features, ".strings"), featureIndex,
    // skipIndex1, skipIndex2);
    // unindexWTP(events, suffix(events, ".strings"), entryIndex, featureIndex,
    // skipIndex1, skipIndex2);
    //
    // // Filter
    //
    //
    //
    // deleteIfExist(eventsFiltered, featuresFiltered, entriesFiltered);
    //
    //
    // filter(events, entries, features, eventsFiltered, entriesFiltered,
    // featuresFiltered, entryIndex, featureIndex, preindexedEntries,
    // preindexedFeatures, skipIndex1, skipIndex2);
    //
    //
    // unindexWT(entriesFiltered, suffix(entriesFiltered, ".strings"),
    // entryIndex, skipIndex1, skipIndex2);
    // unindexWT(featuresFiltered, suffix(featuresFiltered, ".strings"),
    // featureIndex, skipIndex1, skipIndex2);
    // unindexWTP(eventsFiltered, suffix(eventsFiltered, ".strings"),
    // entryIndex, featureIndex, skipIndex1, skipIndex2);
    //
    // // All pairs
    //
    // assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
    // featuresFiltered);
    // deleteIfExist(similarities);
    //
    // AllPairsCommand allpairs = new AllPairsCommand(
    // entriesFiltered, featuresFiltered, eventsFiltered, similarities,
    // charet, new DoubleEnumeratingDelegate(Enumerating.DEFAULT_TYPE,
    // preindexedEntries, preindexedFeatures,
    // null, null, skipIndex1, skipIndex2));
    // allpairs.runCommand();
    //
    // assertValidPlaintextInputFiles(similarities);
    // assertSizeGT(TEST_FRUIT_SIMS, similarities);
    //
    // unindexSims(similarities, suffix(similarities, ".strings"), entryIndex,
    // EnumeratorType.Memory, skipIndex1, skipIndex2, false);
    //
    // // KNN
    //
    //
    // deleteIfExist(neighbours);
    //
    // extknn(similarities, neighbours, preindexedEntries,
    // skipIndex1, skipIndex2);
    //
    // // Finally, convert neighbours back to strings
    //
    // deleteIfExist(neighboursStrings);
    //
    // unindexSims(neighbours, suffix(neighbours, ".strings"), entryIndex,
    // EnumeratorType.Memory, skipIndex1, skipIndex2, false);
    //
    // }
    @Test
    public void BuildTest() throws Exception {
        final String affix = "1-";
        boolean serial = true;
        boolean preindexedEntries = false;
        boolean preindexedFeatures = false;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.Memory;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_parallel() throws Exception {
        final String affix = "2-";
        boolean serial = false;
        boolean preindexedEntries = false;
        boolean preindexedFeatures = false;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.Memory;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_preindex() throws Exception {
        final String affix = "3-";
        boolean serial = true;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.Memory;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_parallel_preindex() throws Exception {
        final String affix = "4-";
        boolean serial = false;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.Memory;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_skipindex() throws Exception {
        final String affix = "5-";
        boolean serial = true;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = true;
        boolean skipIndex2 = true;
        EnumeratorType type = EnumeratorType.Memory;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_parallel_skipindex() throws Exception {
        final String affix = "6-";
        boolean serial = false;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = true;
        boolean skipIndex2 = true;
        EnumeratorType type = EnumeratorType.Memory;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_jdbc() throws Exception {
        final String affix = "7-";
        boolean serial = true;
        boolean preindexedEntries = false;
        boolean preindexedFeatures = false;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.JDBM;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_parallel_jdbc() throws Exception {
        final String affix = "8-";
        boolean serial = false;
        boolean preindexedEntries = false;
        boolean preindexedFeatures = false;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.JDBM;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_preindex_jdbc() throws Exception {
        final String affix = "9-";
        boolean serial = true;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.JDBM;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_parallel_preindex_jdbc() throws Exception {
        final String affix = "10-";
        boolean serial = false;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = false;
        boolean skipIndex2 = false;
        EnumeratorType type = EnumeratorType.JDBM;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_skipindex_jdbc() throws Exception {
        final String affix = "11-";
        boolean serial = true;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = true;
        boolean skipIndex2 = true;
        EnumeratorType type = EnumeratorType.JDBM;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    public void BuildTest_parallel_skipindex_jdbc() throws Exception {
        final String affix = "12-";
        boolean serial = false;
        boolean preindexedEntries = true;
        boolean preindexedFeatures = true;
        boolean skipIndex1 = true;
        boolean skipIndex2 = true;
        EnumeratorType type = EnumeratorType.JDBM;

        parallelBuildTest(affix, serial, type, preindexedEntries,
                preindexedFeatures, skipIndex1, skipIndex2);

    }

    @Test
    @Category(SlowTestCategory.class)
    @Ignore
    public void BuildTestAll() throws Exception {
        for (boolean serial : new boolean[]{true, false}) {
            for (boolean preindexedEntries : new boolean[]{true, false}) {
                for (boolean preindexedFeatures : new boolean[]{true, false}) {
                    for (boolean skipIndex1 : new boolean[]{true, false}) {
                        for (boolean skipIndex2 : new boolean[]{true, false}) {
                            for (EnumeratorType type : EnumeratorType.values()) {
                                String affix = "X";
                                affix += serial ? ".ser" : ".par";
                                affix += preindexedEntries ? ".xe" : "";
                                affix += preindexedFeatures ? ".xf" : "";
                                affix += skipIndex1 ? ".s1" : "";
                                affix += skipIndex2 ? ".s2" : "";
                                affix += "." + type.toString();
                                affix += "__";
                                System.out.println(affix);
                                parallelBuildTest(affix, serial, type,
                                        preindexedEntries, preindexedFeatures,
                                        skipIndex1, skipIndex2);
                            }
                        }
                    }
                }
            }
        }

    }

    public void parallelBuildTest(String affix, boolean serial,
                                  EnumeratorType type, boolean preindexedEntries,
                                  boolean preindexedFeatures, boolean skipIndex1, boolean skipIndex2)
            throws Exception {

        File instances = TEST_FRUIT_INPUT;
        final Charset charet = DEFAULT_CHARSET;

        File instancesIndexed = new File(TEST_OUTPUT_DIR, affix
                + instances.getName() + ".indexed");

        File entryIndex = new File(TEST_OUTPUT_DIR, affix + instances.getName()
                + ".entry-index");
        File featureIndex = new File(TEST_OUTPUT_DIR, affix
                + instances.getName() + ".feature-index");

        File events = new File(TEST_OUTPUT_DIR, affix + instances.getName()
                + ".events");
        File entries = new File(TEST_OUTPUT_DIR, affix + instances.getName()
                + ".entries");
        File features = new File(TEST_OUTPUT_DIR, affix + instances.getName()
                + ".features");

        File eventsFiltered = new File(TEST_OUTPUT_DIR, events.getName()
                + ".filtered");
        File entriesFiltered = new File(TEST_OUTPUT_DIR, entries.getName()
                + ".filtered");
        File featuresFiltered = new File(TEST_OUTPUT_DIR, features.getName()
                + ".filtered");

        File similarities = new File(TEST_OUTPUT_DIR, affix
                + instances.getName() + ".sims");

        File neighbours = new File(TEST_OUTPUT_DIR, similarities.getName()
                + ".neighs");

        File neighboursStrings = new File(TEST_OUTPUT_DIR, neighbours.getName()
                + ".strings");

        if (preindexedEntries || preindexedFeatures) {
            // Index the strings, reproducing the instances file in indexed form

            deleteIfExist(entryIndex, featureIndex, instancesIndexed);

            IndexTPCommandTest.indexTP(instances, instancesIndexed, entryIndex,
                    featureIndex,
                    type, skipIndex1, skipIndex2, false);

            instances = instancesIndexed;
        }
        // Count the entries, features and events

        deleteIfExist(events, entries, features);

        DoubleEnumeratingDelegate countIndex = new DoubleEnumeratingDelegate(
                type, preindexedEntries, preindexedFeatures, entryIndex,
                featureIndex);
        if (serial) {
            CountCommand count = new CountCommand();
            count.setInstancesFile(instances);
            count.setEntriesFile(entries);
            count.setFeaturesFile(features);
            count.setEventsFile(events);
            count.setIndexDelegate(countIndex);
            count.setCharset(charet);
            Assert.assertTrue(count.runCommand());
        } else {
            ExternalCountCommand count = new ExternalCountCommand();
            count.setInstancesFile(instances);
            count.setEntriesFile(entries);
            count.setFeaturesFile(features);
            count.setEventsFile(events);
            count.setIndexDelegate(countIndex);
            count.getFileDelegate().setCharset(charet);
            Assert.assertTrue(count.runCommand());
        }

        assertValidPlaintextInputFiles(entries, features, events);

        // Filter

        deleteIfExist(eventsFiltered, featuresFiltered, entriesFiltered);

        filter(type, events, entries, features, eventsFiltered,
                entriesFiltered, featuresFiltered, entryIndex, featureIndex,
                preindexedEntries, preindexedFeatures, skipIndex1, skipIndex2);

        // All pairs

        assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
                featuresFiltered);
        deleteIfExist(similarities);

        DoubleEnumeratingDelegate allpairsIndex = new DoubleEnumeratingDelegate(
                type, preindexedEntries, preindexedFeatures, entryIndex,
                featureIndex);
        AllPairsCommand allpairs = new AllPairsCommand(entriesFiltered,
                featuresFiltered, eventsFiltered, similarities, charet,
                allpairsIndex);
        if (serial)
            allpairs.setNumThreads(1);
        Assert.assertTrue(allpairs.runCommand());

        assertValidPlaintextInputFiles(similarities);
        assertSizeGT(TEST_FRUIT_SIMS, similarities);

        // KNN

        deleteIfExist(neighbours);

        if (serial) {
            knn(similarities, neighbours, type, preindexedEntries, skipIndex1,
                    skipIndex2);
        } else {
            extknn(similarities, neighbours, type, preindexedEntries,
                    skipIndex1, skipIndex2);
        }

        // Finally, convert neighbours back to strings

        deleteIfExist(neighboursStrings);

        if (preindexedEntries || preindexedFeatures) {
            IndexSimsCommandTest.unindexSims(neighbours,
                    suffix(neighbours, ".strings"), entryIndex,
                    type, skipIndex1, skipIndex2, false);
        }

    }

    private static void filter(EnumeratorType type, File events, File entries,
                               File features, File eventsFiltered, File entriesFiltered,
                               File featuresFiltered, File entryIndex, File featureIndex,
                               boolean preindexedEntries, boolean preindexedFeatures,
                               boolean skipIndex1, boolean skipIndex2) throws Exception {

        assertValidPlaintextInputFiles(events, entries, features);
        assertValidOutputFiles(eventsFiltered, entriesFiltered,
                featuresFiltered);

        FilterCommand filter = new FilterCommand(events, entries, features,
                eventsFiltered, entriesFiltered, featuresFiltered,
                DEFAULT_CHARSET);
        filter.setIndexDelegate(new DoubleEnumeratingDelegate(type,
                preindexedEntries, preindexedFeatures, entryIndex, featureIndex));
        filter.addEventMinimumFrequency(2);

        filter.setTempFiles(new TempFileFactory(TestConstants.TEST_TMP_DIR));
        Assert.assertTrue(filter.runCommand());

        assertValidPlaintextInputFiles(eventsFiltered, entriesFiltered,
                featuresFiltered);
        // assertSizeGT(events, eventsFiltered);
        // assertSizeGT(entries, entriesFiltered);
        // assertSizeGT(features, featuresFiltered);
    }

    private static void knn(File from, File to, EnumeratorType type,
                            boolean enumerated, boolean skip1, boolean skip2) throws Exception {
        assertValidPlaintextInputFiles(from);

        KnnSimsCommand knn = new KnnSimsCommand(from, to, DEFAULT_CHARSET,
                new SingleEnumeratingDelegate(type, enumerated, null), 5);
        Assert.assertTrue(knn.runCommand());

        assertValidPlaintextInputFiles(to);
        assertSizeGT(from, to);
    }

    private static void extknn(File from, File to, EnumeratorType type,
                               boolean enumerated, boolean skip1, boolean skip2) throws Exception {
        assertValidPlaintextInputFiles(from);

        ExternalKnnSimsCommand knn = new ExternalKnnSimsCommand(from, to,
                DEFAULT_CHARSET, new SingleEnumeratingDelegate(type,
                enumerated, null), 5);
        Assert.assertTrue(knn.runCommand());

        assertValidPlaintextInputFiles(to);
        assertSizeGT(from, to);
    }

    // private static void indexTP(File from, File to,
    // File index1, File index2,
    // boolean skip1, boolean skip2)
    // throws Exception {
    // assertValidPlaintextInputFiles(from);
    // assertValidOutputFiles(to);
    //
    // IndexTPCommand unindex = new IndexTPCommand();
    // unindex.getFilesDelegate().setCharset(DEFAULT_CHARSET);
    // unindex.getFilesDelegate().setSourceFile(from);
    // unindex.getFilesDelegate().setDestinationFile(to);
    // unindex.getFilesDelegate().setCompactFormatDisabled(false);
    // unindex.setIndexDelegate(new EnumeratorPairBaringDelegate(
    // true, true, index1, index2, skip1, skip2));
    // unindex.runCommand();
    //
    // assertValidPlaintextInputFiles(to);
    // assertSizeGT(from, to);
    // }
    //
    // private static void unindexSims(File from, File to, File index,
    // boolean skip1, boolean skip2)
    // throws Exception {
    // assertValidPlaintextInputFiles(from);
    //
    // UnindexSimsCommand unindex = new UnindexSimsCommand();
    // unindex.getFilesDelegate().setCharset(DEFAULT_CHARSET);
    // unindex.getFilesDelegate().setSourceFile(from);
    // unindex.getFilesDelegate().setDestinationFile(to);
    // unindex.getFilesDelegate().setCompactFormatDisabled(false);
    // unindex.setIndexDelegate(new EnumeratorSingleBaringDelegate(true, index,
    // null, skip1, skip2));
    // unindex.runCommand();
    //
    // assertValidPlaintextInputFiles(to);
    // assertSizeGT(to, from);
    // }
    private static void unindexWT(EnumeratorType type, File from, File to,
                                  File index, boolean skip1, boolean skip2) throws Exception {
        assertValidPlaintextInputFiles(from);

        IndexingCommands.UnindexEntries unindex = new IndexingCommands.UnindexEntries();
        unindex.getFilesDelegate().setCharset(DEFAULT_CHARSET);
        unindex.getFilesDelegate().setSourceFile(from);
        unindex.getFilesDelegate().setDestinationFile(to);
        unindex.setIndexDelegate(EnumeratingDelegates
                .toPair(new SingleEnumeratingDelegate(type, true, index)));
        unindex.runCommand();

        assertValidPlaintextInputFiles(to);
        assertSizeGT(to, from);
    }

    private static void unindexWTP(EnumeratorType type, File from, File to,
                                   File index1, File index2, boolean skip1, boolean skip2)
            throws Exception {
        assertValidPlaintextInputFiles(from);

        IndexingCommands.IndexEvents unindex = new IndexingCommands.IndexEvents();
        unindex.getFilesDelegate().setCharset(DEFAULT_CHARSET);
        unindex.getFilesDelegate().setSourceFile(from);
        unindex.getFilesDelegate().setDestinationFile(to);
        unindex.setIndexDelegate(new DoubleEnumeratingDelegate(type, true,
                true, index1, index2));
        unindex.runCommand();

        assertValidPlaintextInputFiles(to);
        assertSizeGT(to, from);
    }

    private static void unindexTP(EnumeratorType type, File from, File to,
                                  File index1, File index2, boolean skip1, boolean skip2)
            throws Exception {
        assertValidPlaintextInputFiles(from);

        IndexingCommands.IndexInstances unindex = new IndexingCommands.IndexInstances();
        unindex.getFilesDelegate().setCharset(DEFAULT_CHARSET);
        unindex.getFilesDelegate().setSourceFile(from);
        unindex.getFilesDelegate().setDestinationFile(to);
        unindex.setIndexDelegate(new DoubleEnumeratingDelegate(type, true,
                true, index1, index2));
        unindex.runCommand();

        assertValidPlaintextInputFiles(to);
        assertSizeGT(to, from);
    }

}
