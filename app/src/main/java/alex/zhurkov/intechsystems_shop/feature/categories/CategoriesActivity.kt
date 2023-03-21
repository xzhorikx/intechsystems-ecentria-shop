@file:OptIn(ExperimentalMaterialApi::class)

package alex.zhurkov.intechsystems_shop.feature.categories

import alex.zhurkov.intechsystems_shop.R
import alex.zhurkov.intechsystems_shop.common.arch.UIEvent
import alex.zhurkov.intechsystems_shop.feature.categories.di.CategoriesComponent
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.CategoriesAction
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.CategoriesEvent
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.CategoriesViewModel
import alex.zhurkov.intechsystems_shop.feature.categories.presentation.CategoriesViewModelFactory
import alex.zhurkov.intechsystems_shop.feature.categories.views.MainScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import javax.inject.Inject

class CategoriesActivity : ComponentActivity() {

    private val component: CategoriesComponent by lazy {
        (application as CategoriesComponent.ComponentProvider).provideMainComponent(this)
    }

    @Inject
    lateinit var viewModelFactory: CategoriesViewModelFactory
    private val viewModel by viewModels<CategoriesViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        viewModel.observableEvents.observe(this, Observer(::renderEvent))
        setContent {
            MainScreen(
                modifier = Modifier.fillMaxSize(),
                uiModel = viewModel.observableModel,
                onPullToRefresh = { viewModel.dispatch(CategoriesAction.Refresh) },
                onLastItemVisible = {
                    viewModel.dispatch(CategoriesAction.LastVisibleItemChanged(id = it))
                },
                onClick = {
//                    this@HomeActivity.startActivity(
//                        Intent(this@HomeActivity, RepoInfoActivity::class.java).putExtras(
//                            RepoInfoInputData(repoId = it.id.toLong()).toBundle()
//                        )
//                    )
                },
            )
        }
    }

    private fun renderEvent(event: UIEvent) {
        if (event is CategoriesEvent) {
            when (event) {
                is CategoriesEvent.DisplayError -> {
                    Toast.makeText(
                        this,
                        getString(R.string.error_message_template, event.e),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is CategoriesEvent.NetworkConnectionChanged -> {
                    @StringRes val textRes = when (event.isConnected) {
                        true -> R.string.network_restored
                        false -> R.string.error_network_disconnected
                    }
                    Toast.makeText(this, getString(textRes), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}