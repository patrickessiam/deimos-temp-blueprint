object BaseConfig {
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0
    private const val versionQualifier = "alpha1"
    const val debugBuidType = "debug"

    const val Id = "com.deimosdev.androidblueprint"
    val VersionCode = generateVersionCode()
    val VersionName = generateVersionName()
    val BASE_URL = '"'
    val DEV_API_URL = "\"\""
    val PROD_API_URL = "\"\""
    val DEV_APP_LABEL = "Android Blueprint Dev"
    val STAGING_APP_LABEL = "Android Blueprint Staging"
    val PRODUCTION_APP_LABEL = "Android Blueprint Prod"
    val DEFAULT_APP_LABEL = "Android Blueprint"

    const val MinSdk = 21
    const val TargetSdk = 33
    const val CompileSdk = 33
    const val AndroidJunitRunner = "androidx.test.runner.AndroidJUnitRunner"

    private fun generateVersionCode(): Int {
        return versionMajor * 10000 + versionMinor * 100 + versionPatch
    }

    private fun generateVersionName(): String {
        return "$versionMajor.$versionMinor.$versionPatch"
    }

}
