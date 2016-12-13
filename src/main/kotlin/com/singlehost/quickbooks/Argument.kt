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
import java.util.Objects
import java.util.function.Function
import java.util.stream.Stream

object Argument {
  /**
   * Checks if the provided objects are all non-null.
   *
   * @param objects The objects to check for nullity.
   * @return `true` if all objects are not null; Or, `false`, otherwise.
   */
  fun allNonNull(vararg objects: Any): Boolean = Stream.of(*objects).allMatch { it != null }

  /**
   * Ensures that the provided value is within the specified range of values,
   * inclusively.
   *
   * @param value The value of the argument being checked.
   * @param min The minimum inclusive value for the argument.
   * @param max The maximum inclusive value for the argument.
   * @param name The human-friendly name for the argument (for error messages).
   */
  fun ensureInRange(value: Double, min: Double, max: Double, name: String) {
    if (value < min || value > max) {
      throw IllegalArgumentException("$name must be between $min and $max, inclusive (was given `$value`).")
    }
  }

  /**
   * Ensures that the provided [BigDecimal] value is equal to a
   * negative value, or zero.
   *
   * @param value The value of the argument being checked.
   * @param name The human-friendly name for the argument (for error messages).
   */
  fun ensureNegativeOrZero(value: BigDecimal, name: String) {
    if (value.signum() > 0) throw IllegalArgumentException("$name cannot be positive (was given `$value`).")
  }

  /**
   * Ensures that the provided [BigDecimal] value is equal to a positive value, or zero.
   *
   * @param value The value of the argument being checked.
   * @param name The human-friendly name for the argument (for error messages).
   */
  fun ensurePositiveOrZero(value: BigDecimal, name: String) {
    if (value.signum() < 0) throw IllegalArgumentException("$name cannot be negative (was given `$value`).")
  }

  /**
   * Ensures that the provided argument is not null.
   *
   * @param value The value of the argument being checked.
   * @param name The human-friendly name for the argument (for error messages).
   * @throws IllegalArgumentException If `value` is `null`.
   */
  fun ensureNotNull(value: Any?, name: String) {
    if (value == null) throw IllegalArgumentException("$name cannot be null")
  }

  /**
   * Ensures that all of the provided argument values are either null or all
   * have a non-null value.
   *
   * This is useful for a group of arguments that are optional, but can
   * only take effect if all are specified.
   *
   * If at least one value is set, but not all of the values are set,
   * an [IllegalArgumentException] is raised with the provided error
   * message.
   *
   * @param errorMessage The error message to include in an exception, if one of the values is `null` but the rest are not.
   * @param values The values to check.
   * @throws IllegalArgumentException If one of the values is `null` but the rest are not.
   */
  fun ensureAllOrNoneNull(errorMessage: String, vararg values: Any) {
    var positionOfFirstNull: Int? = null
    var positionOfFirstSet: Int? = null

    values.indices.forEach {
      if (positionOfFirstNull == null) positionOfFirstNull = it
      if (positionOfFirstSet == null) positionOfFirstSet = it
    }

    if (positionOfFirstSet != null && positionOfFirstNull != null) {
      throw IllegalArgumentException("$errorMessage (argument ${positionOfFirstNull as Int + 1} is `null`).")
    }
  }

  /**
   * Ensures that either the current value is not yet set, or that it matches
   * the provided new value.
   *
   * @param currentValue The current value of the field.
   * @param newValue The proposed new value for the field.
   * @param name The human-friendly name for the field (for error messages).
   * @throws IllegalStateException If the field is already set to a different value.
   */
  fun ensureUnset(currentValue: Int, newValue: Int, name: String) {
    if (currentValue != 0 && currentValue != newValue) {
      throw IllegalStateException("The $name can only be set once (already set to `$currentValue`; was trying to set to `$newValue`).")
    }
  }

  /**
   * Ensures that either the current value is not yet set, or that it matches
   * the provided new value.
   *
   * @param currentValue The current value of the field.
   * @param newValue The proposed new value for the field.
   * @param name The human-friendly name for the field (for error messages).
   * @throws IllegalStateException If the field is already set to a different value.
   */
  fun ensureUnset(currentValue: Double, newValue: Double, name: String) {
    if (currentValue != 0.0 && currentValue != newValue) {
      throw IllegalStateException("The $name can only be set once (already set to `$currentValue`; was trying to set to `$newValue`).")
    }
  }

  /**
   * Ensures that either the current value is not yet set, or that it matches
   * the provided new value.
   *
   * @param currentValue The current value of the field.
   * @param newValue The proposed new value for the field.
   * @param name The human-friendly name for the field (for error messages).
   * @throws IllegalStateException If the field is already set to a different value.
   */
  fun ensureUnset(currentValue: Float, newValue: Float, name: String) {
    if (currentValue != 0f && currentValue != newValue) {
      throw IllegalStateException("The $name can only be set once (already set to `$currentValue`; was trying to set to `$newValue`).")
    }
  }

  /**
   * Ensures that either the current value is not yet set, an empty
   * collection, or the same collection as the provided new value.
   *
   * @param currentValue The current value of the field.
   * @param newValue The proposed new value for the field.
   * @param name The human-friendly name for the field (for error messages).
   * @throws IllegalStateException If the field is already set to a different value.
   */
  fun ensureUnset(currentValue: Collection<*>?, newValue: Collection<*>, name: String) {
    if (currentValue != null && !currentValue.isEmpty() && currentValue != newValue) {
      throw IllegalStateException("The $name can only be set when empty (it currently contains `${currentValue.size}` elements).")
    }
  }

  /**
   * Ensures that either the current value is not yet set, or that it matches
   * the provided new value.
   *
   * @param currentValue The current value of the field.
   * @param newValue The proposed new value for the field.
   * @param name The human-friendly name for the field (for error messages).
   * @throws IllegalStateException  If the field is already set to a different value.
   */
  fun ensureUnset(currentValue: Any, newValue: Any, name: String) =
    ensureUnset(currentValue, newValue, Function<Any, Boolean> { Objects.isNull(it) }, name)


  /**
   * Ensures that either the current value is empty, as defined by the
   * provided function, or that it matches the provided new value.
   *
   * @param currentValue The current value of the field.
   * @param newValue The proposed new value for the field.
   * @param emptyFunc The function used to test whether the current value is empty.
   * @param name The human-friendly name for the field (for error messages).
   * @throws IllegalStateException If the field is already set to a different value.
   */
  fun <T> ensureUnset(
    currentValue: T,
    newValue: T,
    emptyFunc: Function<T, Boolean>,
    name: String
  ) {
    if (!emptyFunc.apply(currentValue) && currentValue != newValue) {
      throw IllegalStateException("The $name can only be set once (already set to `$currentValue`; was trying to set to `$newValue`).")
    }
  }
}
