package com.deimosdev.testutils

import org.junit.Rule

open class MockKConfig {
    open fun onCreate() {}

    open fun onDestroy() {}

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()
}
