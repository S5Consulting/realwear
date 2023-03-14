package com.akso.realwear.app.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sap.cloud.mobile.fiori.onboarding.LaunchScreen
import com.sap.cloud.mobile.fiori.onboarding.ext.LaunchScreenSettings
import com.sap.cloud.mobile.flowv2.core.DialogHelper
import com.sap.cloud.mobile.flowv2.core.Flow
import com.sap.cloud.mobile.flowv2.core.FlowContextBuilder
import com.sap.cloud.mobile.flowv2.ext.FlowOptions
import com.sap.cloud.mobile.flowv2.ext.ButtonSingleClickListener
import com.sap.cloud.mobile.flowv2.ext.SignedQRCodeOption
import com.sap.cloud.mobile.foundation.configurationprovider.*
import com.sap.cloud.mobile.foundation.model.AppConfig
import com.akso.realwear.R
import com.akso.realwear.app.app.workOrders.WorkOrderItemAdapter

class WelcomeActivity : AppCompatActivity() {
    private val fManager = this.supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkOrderItemAdapter.setContext(this)

        val welcomeLaunchScreen = LaunchScreen(this)
        welcomeLaunchScreen.initialize(
            LaunchScreenSettings.Builder()
                .setDemoButtonVisible(false)
                .setHeaderLineLabel(getString(R.string.welcome_screen_headline_label))
                .setPrimaryButtonText(getString(R.string.welcome_screen_primary_button_label))
                .setFooterVisible(true)
                .setUrlTermsOfService("http://www.sap.com")
                .setUrlPrivacy("http://www.sap.com")
                .addInfoViewSettings(
                    LaunchScreenSettings.LaunchScreenInfoViewSettings(
                        R.drawable.ic_android_white_circle_24dp,
                        getString(R.string.application_name),
                        getString(R.string.welcome_screen_detail_label)
                    )
                )
                .build()
        )
        welcomeLaunchScreen.setPrimaryButtonOnClickListener(ButtonSingleClickListener{
            startConfigurationLoader()
        })
        setContentView(welcomeLaunchScreen)
    }

    private fun startConfigurationLoader() {
        val callback: ConfigurationLoaderCallback = object : ConfigurationLoaderCallback() {
            override fun onCompletion(providerIdentifier: ProviderIdentifier?, success: Boolean) {
                if (success) {
                    startFlow(this@WelcomeActivity)
                } else {
                    DialogHelper(application, R.style.OnboardingDefaultTheme_Dialog_Alert)
                            .showOKOnlyDialog(
                                    fManager,
                                    resources.getString(R.string.config_loader_complete_error_description),
                                    null, null, null
                            )
                }
            }

            override fun onError(configurationLoader: ConfigurationLoader, providerIdentifier: ProviderIdentifier, userInputs: UserInputs, configurationProviderError: ConfigurationProviderError) {
                DialogHelper(application, R.style.OnboardingDefaultTheme_Dialog_Alert)
                        .showOKOnlyDialog(
                                fManager, String.format(resources.getString(
                                R.string.config_loader_on_error_description),
                                providerIdentifier.toString(), configurationProviderError.errorMessage
                        ),
                                null, null, null
                        )
                configurationLoader.processRequestedInputs(UserInputs())
            }

            override fun onInputRequired(configurationLoader: ConfigurationLoader, userInputs: UserInputs) {
                configurationLoader.processRequestedInputs(UserInputs())
            }
        }
        val providers = arrayOf<ConfigurationProvider>(FileConfigurationProvider(this, "sap_mobile_services"))
        this.runOnUiThread {
            val loader = ConfigurationLoader(this, callback, providers)
            loader.loadConfiguration()
        }
    }

    private fun prepareAppConfig(): AppConfig? {
        return try {
            val configData = DefaultPersistenceMethod.getPersistedConfiguration(this)
            AppConfig.createAppConfigFromJsonString(configData.toString())
        } catch (ex: ConfigurationPersistenceException) {
            DialogHelper(this, R.style.OnboardingDefaultTheme_Dialog_Alert)
                    .showOKOnlyDialog(
                            fManager,
                            resources.getString(R.string.config_data_build_json_description),
                            null, null, null
                    )
            null
        } catch (ex: Exception) {
            DialogHelper(this, R.style.OnboardingDefaultTheme_Dialog_Alert)
                    .showOKOnlyDialog(
                            fManager,
                            ex.localizedMessage ?: resources.getString(R.string.error_unknown_app_config),
                            null, null, null
                    )
            null
        }
    }

    internal fun startFlow(activity: Activity) {
        val appConfig = prepareAppConfig() ?: return
        val flowContext =
                FlowContextBuilder()
                    .setApplication(appConfig)
                    .setMultipleUserMode(false)
                    .setFlowStateListener(WizardFlowStateListener(activity.application as SAPWizardApplication))
                    .setFlowOptions(FlowOptions(
                            needConfirmWhenDeleteRegistration = false,
                            signedQRCodeOption = SignedQRCodeOption.SIGNED_ONLY
                    ))
                    .build()
        Flow.start(activity, flowContext) { _, resultCode, _ ->
            if (resultCode == Activity.RESULT_OK) {
                (application as SAPWizardApplication).isApplicationUnlocked = true
                Intent(application, MainBusinessActivity::class.java).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    application.startActivity(it)
                }
            }
        }
    }
}
