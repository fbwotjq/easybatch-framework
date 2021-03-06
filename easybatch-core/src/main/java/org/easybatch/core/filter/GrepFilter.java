/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.core.filter;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.record.StringRecord;

/**
 * Convenient filter that mimics the unix grep utility: it keeps records containing the given pattern
 * instead of filtering them.
 * <p/>
 * Should be used with {@link org.easybatch.core.record.StringRecord} type. Search is case sensitive.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GrepFilter implements RecordFilter {

    private String pattern;

    private boolean negate;

    public GrepFilter(String pattern) {
        this.pattern = pattern;
    }

    public GrepFilter(String pattern, boolean negate) {
        this.pattern = pattern;
        this.negate = negate;
    }

    @Override
    public boolean filterRecord(Record record) {
        StringRecord stringRecord = (StringRecord) record;
        String payload = stringRecord.getPayload();
        boolean result = doFilterRecord(payload);
        return negate ? !result : result;
    }

    private boolean doFilterRecord(String payload) {
        return !payload.contains(pattern);
    }

}
