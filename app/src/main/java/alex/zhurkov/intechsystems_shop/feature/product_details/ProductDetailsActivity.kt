package alex.zhurkov.intechsystems_shop.feature.product_details

import alex.zhurkov.intechsystems_shop.R
import alex.zhurkov.intechsystems_shop.common.arch.UIEvent
import alex.zhurkov.intechsystems_shop.feature.categories.CategoriesActivity
import alex.zhurkov.intechsystems_shop.feature.product_details.di.ProductDetailsComponent
import alex.zhurkov.intechsystems_shop.feature.product_details.model.getInputData
import alex.zhurkov.intechsystems_shop.feature.product_details.presentation.ProductDetailsViewModel
import alex.zhurkov.intechsystems_shop.feature.product_details.presentation.ProductDetailsViewModelFactory
import alex.zhurkov.intechsystems_shop.feature.product_details.views.ProductDetailsScreen
import alex.zhurkov.intechsystems_shop.feature.products.presentation.ProductsEvent
import android.content.Intent
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

class ProductDetailsActivity : ComponentActivity() {

    private val component: ProductDetailsComponent by lazy {
        (application as ProductDetailsComponent.ComponentProvider).provideProductDetailsComponent(
            activity = this,
            inputData = intent.extras.getInputData()
        )
    }

    @Inject
    lateinit var viewModelFactory: ProductDetailsViewModelFactory
    private val viewModel by viewModels<ProductDetailsViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        viewModel.observableEvents.observe(this, Observer(::renderEvent))
        setContent {
            ProductDetailsScreen(
                modifier = Modifier.fillMaxSize(),
                uiModel = viewModel.observableModel,
                onHomeClick = {
                    this@ProductDetailsActivity.startActivity(
                        Intent(this@ProductDetailsActivity, CategoriesActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        }
                    )
                },
                onBackClick = { this@ProductDetailsActivity.finish() },
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