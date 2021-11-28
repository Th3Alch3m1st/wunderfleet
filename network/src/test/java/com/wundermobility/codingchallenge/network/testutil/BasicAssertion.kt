package com.wundermobility.codingchallenge.network.testutil

import org.assertj.core.api.Assertions.assertThat

/**
 * Created By Rafiqul Hasan
 */

infix fun <T> T.shouldEqual(expected: T?): T = this.apply { assertThat(this).isEqualTo(expected) }