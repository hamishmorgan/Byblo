/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.susx.mlcl.byblo.commands;

import uk.ac.susx.mlcl.byblo.io.IndexDeligatePair;
import com.beust.jcommander.ParametersDelegate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.susx.mlcl.byblo.io.*;
import uk.ac.susx.mlcl.lib.Checks;
import uk.ac.susx.mlcl.lib.io.Sink;
import uk.ac.susx.mlcl.lib.io.Source;

/**
 *
 * @author hiam20
 */
public class UnindexSimsCommand extends AbstractCopyCommand<Weighted<TokenPair>> {

    private static final Log LOG = LogFactory.getLog(UnindexSimsCommand.class);

    @ParametersDelegate
    private IndexDeligate indexDeligate = new IndexDeligate();

//    private Enumerator<String> index = null;
    public UnindexSimsCommand(
            File sourceFile, File destinationFile, Charset charset, File indexFile) {
        super(sourceFile, destinationFile, charset);
        indexDeligate.setIndexFile(indexFile);
    }
//
//    public UnindexSimsCommand(File sourceFile, File destinationFile) {
//        super(sourceFile, destinationFile);
//    }

    public UnindexSimsCommand() {
        super();
    }

    @Override
    public void runCommand() throws Exception {
        Checks.checkNotNull("indexFile", indexDeligate.getIndexFile());

//        index = Enumerators.loadStringEnumerator(indexFile);
//        Checks.checkNotNull("index", index);
        super.runCommand();
    }

    @Override
    protected Source<Weighted<TokenPair>> openSource(File file)
            throws FileNotFoundException, IOException {
        IndexDeligatePair dstIdx = new IndexDeligatePair(true, true);
        dstIdx.setSkipindexed1(indexDeligate.isSkipIndexed());
        dstIdx.setSkipindexed2(indexDeligate.isSkipIndexed());
        return WeightedTokenPairSource.open(
                file, getFilesDeligate().getCharset(),
                dstIdx);
    }

    @Override
    protected Sink<Weighted<TokenPair>> openSink(File file)
            throws FileNotFoundException, IOException {
        IndexDeligatePair srcIdx = new IndexDeligatePair(
                false, false, 
                indexDeligate.getEnumerator(), 
                indexDeligate.getEnumerator(), false, false);
        return WeightedTokenPairSink.open(
                file, getFilesDeligate().getCharset(),
                srcIdx, true);
    }

//    public final File getIndexFile() {
//        return indexFile;
//    }
//
//    public final void setIndexFile(File indexFile) {
//        Checks.checkNotNull("indexFile", indexFile);
//        this.indexFile = indexFile;
//    }
    public IndexDeligate getIndexDeligate() {
        return indexDeligate;
    }

    public void setIndexDeligate(IndexDeligate indexDeligate) {
        this.indexDeligate = indexDeligate;
    }

}
