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
package uk.ac.susx.mlcl.lib.tasks;

import com.google.common.base.Objects;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

import static java.text.MessageFormat.format;

/**
 * Abstract super class to all task objects, implementing common functionality.
 *
 * @author Hamish I A Morgan &lt;hamish.morgan@sussex.ac.uk&gt;
 */
public abstract class AbstractTask implements Task {

    private static final Log LOG = LogFactory.getLog(AbstractTask.class);

    private final Properties properties = new Properties();

    private final ExceptionTrappingDelegate exceptionDelegate =
            new ExceptionTrappingDelegate();

    public AbstractTask() {
    }

    protected void initialiseTask() throws Exception {
    }

    protected abstract void runTask() throws Exception;

    protected void finaliseTask() throws Exception {
    }

    @Override
    public final void run() {
        try {
            if (LOG.isTraceEnabled())
                LOG.trace(format("Initialising task: {0}", this));

            initialiseTask();

            if (LOG.isTraceEnabled())
                LOG.trace(format("Running task: ", this));

            runTask();

        } catch (Exception ex) {
            exceptionDelegate.trapException(ex);
        } catch (Throwable t) {
            exceptionDelegate.trapException(new RuntimeException(t));
        } finally {

            try {

                if (LOG.isTraceEnabled())
                    LOG.trace(format("Finalising task: {0}", this));
                finaliseTask();

            } catch (Exception ex) {
                exceptionDelegate.trapException(ex);
            } catch (Throwable t) {
                exceptionDelegate.trapException(new RuntimeException(t));
            }

            if (LOG.isTraceEnabled())
                LOG.trace(format("Completed task: {0}" + this));
        }
    }

    @Override
    public final String toString() {
        return toStringHelper().toString();
    }

    protected Objects.ToStringHelper toStringHelper() {
        return Objects.toStringHelper(this).
                add("exceptions", getExceptionDelegate()).
                add("properties", properties);
    }

    boolean equals(AbstractTask other) {
        return !(this.properties != other.properties && (this.properties == null || !this.properties.equals(other.properties))) && !(this.exceptionDelegate != other.exceptionDelegate && (this.exceptionDelegate == null || !this.exceptionDelegate.equals(other.exceptionDelegate)));
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && equals((AbstractTask) obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (this.properties != null ? this.properties.hashCode() : 0);
        hash = 11 * hash + (this.exceptionDelegate != null ? this.exceptionDelegate.hashCode() : 0);
        return hash;
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    ExceptionTrappingDelegate getExceptionDelegate() {
        return exceptionDelegate;
    }

    @Override
    public final synchronized void throwTrappedException() throws Exception {
        exceptionDelegate.throwTrappedException();
    }

    @Override
    public final synchronized boolean isExceptionTrapped() {
        return exceptionDelegate.isExceptionTrapped();
    }

}
