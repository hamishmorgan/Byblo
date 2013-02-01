/*
 * Copyright (c) 2010-2013, University of Sussex
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
package uk.ac.susx.mlcl.lib.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.susx.mlcl.lib.Checks;

import java.text.MessageFormat;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of ProgressReporting that can be used as a delegate by
 * containing objects.
 *
 * @author Hamish Morgan &lt;hamish.morgan@sussex.ac.uk&gt;
 */
public class ProgressDelegate implements ProgressReporting {

    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(ProgressDelegate.class);

    private final CopyOnWriteArrayList<ProgressListener> progressListeners =
            new CopyOnWriteArrayList<ProgressListener>();

    private final ProgressReporting outer;

    private final boolean progressPercentageSupported;

    private int progressPercent = 0;

    private ProgressEvent event = null;

    private String message = null;

    private State state = State.PENDING;

    private boolean stateChangedSinceLastEvent = false;

    private final AtomicInteger adjustingCount = new AtomicInteger(0);

    public ProgressDelegate(ProgressReporting outer, boolean progressPercentageSupported) {
        if (outer == this)
            throw new IllegalArgumentException("outer == this");
        this.outer = outer;
        this.progressPercentageSupported = progressPercentageSupported;
    }

    public ProgressDelegate(ProgressReporting outer) {
        if (outer == this)
            throw new IllegalArgumentException("outer == this");
        this.outer = outer;
        this.progressPercentageSupported = true;
    }

    public ProgressDelegate(boolean progressPercentageSupported) {
        this.outer = null;
        this.progressPercentageSupported = progressPercentageSupported;
    }

    public ProgressDelegate() {
        this.outer = null;
        this.progressPercentageSupported = true;
    }

    protected boolean isStateChangedSinceLastEvent() {
        return stateChangedSinceLastEvent;
    }

    void setStateChangedSinceLastEvent() {
        this.stateChangedSinceLastEvent = true;
    }

    ProgressReporting getOuter() {
        return outer;
    }

    @Override
    public String getName() {
        if (outer == null)
            return "<unnamed delegate>";
        else
            return outer.getName();
    }

    /**
     * Indicate the start of series of updates that should be considered a
     * single atomic transaction.
     * <p/>
     * This method is used (in conjunction with {@link #endAdjusting() } to avoid
     * multiple events being fired during a sequence of state changes.
     *
     * @throws IllegalStateException if adjustingCount is negative
     */
    public void startAdjusting() throws IllegalStateException {
        if (adjustingCount.get() < 0)
            throw new IllegalStateException("adjustingCount is negative");

        adjustingCount.incrementAndGet();
    }

    /**
     * Indicate the end of series of updates that should be considered a single
     * atomic transaction.
     * <p/>
     * This method is used (in conjunction with {@link #startAdjusting() } to
     * avoid multiple events being fired during a sequence of state changes.
     *
     * @throws IllegalStateException if no transaction has been started
     */
    public void endAdjusting() {
        if (adjustingCount.get() < 1)
            throw new IllegalStateException(
                    "attempting to end a transaction that has not been started");

        if (adjustingCount.decrementAndGet() == 0) {
            fireProgressChangedEvent();
        }
    }

    @Override
    public int getProgressPercent() {
        if (!isProgressPercentageSupported())
            throw new UnsupportedOperationException(
                    "Progress percentage reporting is not supported.");
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        if (!isProgressPercentageSupported())
            throw new UnsupportedOperationException(
                    "Progress percentage reporting is not supported.");
        Checks.checkRangeIncl(progressPercent, 0, 100);
        if (progressPercent != this.progressPercent) {
            startAdjusting();
            this.progressPercent = progressPercent;
            stateChangedSinceLastEvent = true;
            endAdjusting();
        }
    }

    @Override
    public boolean isProgressPercentageSupported() {
        return progressPercentageSupported;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State newState) {
        Checks.checkNotNull(newState);
        if (state == newState)
            return;
        startAdjusting();
        this.state = newState;
        stateChangedSinceLastEvent = true;
        endAdjusting();
    }

    public void setMessage(String newMessage) {
        Checks.checkNotNull(newMessage);
        if (this.message == null || !this.message.equals(newMessage)) {
            startAdjusting();
            this.message = newMessage;
            stateChangedSinceLastEvent = true;
            endAdjusting();
        }
    }

    @Override
    public String getProgressReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        sb.append(": ");
        if (message != null) {
            sb.append(message);
            sb.append(' ');
        }
        sb.append("(");
        sb.append(isProgressPercentageSupported()
                ? getProgressPercent() : "unknown");
        sb.append("% complete)");
        return sb.toString();
    }

    private static volatile int linkCount = 0;

    @Override
    public void addProgressListener(ProgressListener progressListener) {
        Checks.checkNotNull(progressListener);
        if (!progressListeners.contains(progressListener)) {
            if (progressListeners.add(progressListener))
                ++linkCount;

            // While progress reporters don't have to extend (or delegate) this
            // class to implement ProgressReporting, it is by far the easiest
            // way. As such most objects will be using an instance of this class
            // and linkCount will approximate the total number of attached
            // listeners. It's easy to forget to remove them so this check
            // prints a warning when it grown large.
            if (linkCount >= 100 && linkCount % 100 == 0 && LOG.isWarnEnabled())
                LOG.warn(MessageFormat.format("Listener link count has grown "
                        + "to {0}; indicating there may be a leak.", linkCount));
        }

    }

    @Override
    public void removeProgressListener(ProgressListener progressListener) {
        Checks.checkNotNull(progressListener);
        if (progressListeners.remove(progressListener))
            --linkCount;
        assert linkCount >= 0;
    }

    @Override
    public ProgressListener[] getProgressListeners() {
        return progressListeners.toArray(new ProgressListener[progressListeners.size()]);
    }

    void fireProgressChangedEvent() {
        if (!stateChangedSinceLastEvent)
            return;
        stateChangedSinceLastEvent = false;
        if (progressListeners.isEmpty())
            return;
        if (event == null)
            event = new ProgressEvent(outer == null ? this : outer);
        for (ProgressListener progressListener : progressListeners) {
            try {
                progressListener.progressChanged(event);
            } catch (RuntimeException ex) {
                LOG.error(MessageFormat.format("Progress listener {0} threw "
                        + "RuntimeException while handing event {1}; removing "
                        + "from observer pool.", progressListener, event), ex);
                removeProgressListener(progressListener);
            }
        }
    }

    boolean equals(ProgressDelegate other) {
        if (this.progressListeners != other.progressListeners && (this.progressListeners == null || !this.progressListeners.equals(other.progressListeners)))
            return false;
        return this.progressPercent == other.progressPercent && !(this.outer != other.outer && (this.outer == null || !this.outer.equals(other.outer))) && !(this.event != other.event && (this.event == null || !this.event.equals(other.event)));
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && equals((ProgressDelegate) obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.progressListeners != null
                ? this.progressListeners.hashCode() : 0);
        hash = 97 * hash + this.progressPercent;
        hash = 97 * hash + (this.outer != null ? this.outer.hashCode() : 0);
        hash = 97 * hash + (this.event != null ? this.event.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ProgressDelegate{"
                + "progressListeners=" + progressListeners
                + ", progressPercent=" + progressPercent
                + '}';
    }
}
