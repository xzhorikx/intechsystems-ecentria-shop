package alex.zhurkov.intechsystems_shop.feature.products

import alex.zhurkov.intechsystems_shop.R
import alex.zhurkov.intechsystems_shop.common.arch.UIEvent
import alex.zhurkov.intechsystems_shop.feature.products.di.ProductsComponent
import alex.zhurkov.intechsystems_shop.feature.products.model.getInputData
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsAction
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsEvent
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsViewModel
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsViewModelFactory
import alex.zhurkov.intechsystems_shop.feature.products.views.ProductsScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import javax.inject.Inject

class ProductsActivity : ComponentActivity() {

    private val component: ProductsComponent by lazy {
        (application as ProductsComponent.ComponentProvider).provideProductsComponent(
            activity = this,
            inputData = intent.extras.getInputData()
        )
    }

    @Inject
    lateinit var viewModelFactory: ProductsViewModelFactory
    private val viewModel by viewModels<ProductsViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        viewModel.observableEvents.observe(this, Observer(::renderEvent))
        setContent {
            ProductsScreen(
                modifier = Modifier.fillMaxSize(),
                uiModel = viewModel.observableModel,
                onBackClick = { this@ProductsActivity.finish() },
                onPullToRefresh = { viewModel.dispatch(ProductsAction.Refresh) },
                onLastItemVisible = {
                    viewModel.dispatch(ProductsAction.LastVisibleItemChanged(id = it))
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
        if (event is ProductsEvent) {
            when (event) {
                is ProductsEvent.DisplayError -> {
                    Toast.makeText(
                        this,
                        getString(R.string.error_message_template, event.e),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ProductsEvent.NetworkConnectionChanged -> {
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