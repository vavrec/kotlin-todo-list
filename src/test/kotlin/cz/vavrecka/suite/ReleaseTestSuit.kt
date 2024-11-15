package cz.vavrecka.suite

import cz.vavrecka.TestTags
import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages("cz.vavrecka")
@IncludeTags(value = [TestTags.UNIT_TEST, TestTags.COMPONENT_TEST, TestTags.INTEGRATION_TEST])
class ReleaseTestSuit {
}