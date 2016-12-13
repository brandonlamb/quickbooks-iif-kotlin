/*
 * Copyright (C) 2016 Red Bottle Design, LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.singlehost.quickbooks

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Comparator

/**
 * A comparator for {@link BigDecimal} values that represent money values.
 *
 * @author Guy Paddock (guy@redbottledesign.com)
 */
class MoneyComparator : Comparator<BigDecimal> {
  override fun compare(first: BigDecimal, second: BigDecimal): Int = applyPrecision(first).compareTo(applyPrecision(second))

  companion object {
    /**
     * Applies the standard US dollar precision of two decimal places, with
     * appropriate rounding [RoundingMode.HALF_UP].
     *
     * TODO: Support [RoundingMode.HALF_EVEN] at some point.
     *
     * @param value The value to round to two decimal places.
     * @return A new [BigDecimal] value with the appropriate precision.
     */
    fun applyPrecision(value: BigDecimal): BigDecimal = value.setScale(2, RoundingMode.HALF_UP)

    /**
     * Compares two [BigDecimal] values for equality, treating both as
     * monetary values.
     *
     * @param first The first monetary value.
     * @param second The second monetary value.
     * @return Whether or not the two values are equal when compared as monetary values.
     */
    fun areAmountsEqual(first: BigDecimal, second: BigDecimal): Boolean = MoneyComparator().compare(first, second) == 0
  }
}
