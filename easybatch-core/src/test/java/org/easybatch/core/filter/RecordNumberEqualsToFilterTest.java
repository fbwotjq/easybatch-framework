/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.easybatch.core.api.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for {@link org.easybatch.core.filter.RecordNumberEqualsToFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class RecordNumberEqualsToFilterTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Record record;
    
    private RecordNumberEqualsToFilter recordNumberEqualsToFilter;

    @Test
    public void whenTheRecordNumberIsEqualToExpectedNumber_ThenItShouldBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(1);

        when(record.getHeader().getNumber()).thenReturn(1l);
        assertThat(recordNumberEqualsToFilter.filterRecord(record)).isTrue();
    }

    @Test
    public void whenTheRecordNumberIsEqualToOneOfTheExpectedNumbers_ThenItShouldBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(1, 2);

        when(record.getHeader().getNumber()).thenReturn(1l);
        assertThat(recordNumberEqualsToFilter.filterRecord(record)).isTrue();

        when(record.getHeader().getNumber()).thenReturn(2l);
        assertThat(recordNumberEqualsToFilter.filterRecord(record)).isTrue();
    }

    /*
     * Test the negate behavior
     */

    @Test
    public void whenTheRecordNumberIsEqualToExpectedNumber_ThenItShouldNotBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(true, 1);

        when(record.getHeader().getNumber()).thenReturn(1l);
        assertThat(recordNumberEqualsToFilter.filterRecord(record)).isFalse();
    }

    @Test
    public void whenTheRecordNumberIsEqualToOneOfTheExpectedNumbers_ThenItShouldNotBeFiltered() {
        recordNumberEqualsToFilter = new RecordNumberEqualsToFilter(true, 1, 2);

        when(record.getHeader().getNumber()).thenReturn(1l);
        assertThat(recordNumberEqualsToFilter.filterRecord(record)).isFalse();
        when(record.getHeader().getNumber()).thenReturn(2l);
        assertThat(recordNumberEqualsToFilter.filterRecord(record)).isFalse();
    }

}
