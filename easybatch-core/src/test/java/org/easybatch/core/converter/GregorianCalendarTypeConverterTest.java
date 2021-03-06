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

package org.easybatch.core.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link GregorianCalendarTypeConverter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GregorianCalendarTypeConverterTest extends BaseConverterTest<GregorianCalendar> {

    @Before
    public void setUp() throws Exception {
        converter = new GregorianCalendarTypeConverter();
    }

    @Test
    public void whenInputValueIsLegalValue_ThenShouldReturnValidDate() {
        String date = "2015-01-01";
        GregorianCalendar convertedCalendar = converter.convert(date);
        assertThat(convertedCalendar).isNotNull();
        assertThat(convertedCalendar.getTime()).isNotNull();
        assertThat(convertedCalendar.getTime()).isEqualTo(java.sql.Date.valueOf(date));
    }

}
