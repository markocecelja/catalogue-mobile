package com.mcecelja.catalogue.ui.product

import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcecelja.catalogue.Catalogue
import com.mcecelja.catalogue.data.PreferenceManager
import com.mcecelja.catalogue.data.dto.ResponseMessage
import com.mcecelja.catalogue.data.dto.product.ProductDTO
import com.mcecelja.catalogue.services.ProductService
import com.mcecelja.catalogue.ui.LoadingViewModel
import com.mcecelja.catalogue.utils.AlertUtil
import com.mcecelja.catalogue.utils.RestUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    private val _products: MutableLiveData<List<ProductDTO>> = MutableLiveData<List<ProductDTO>>()
    val products: LiveData<List<ProductDTO>> = _products

    fun setProducts(products: List<ProductDTO>?) {
        _products.value = products
    }

    fun changeFavouriteStatusForPosition(position: Int, activity: FragmentActivity?, loadingViewModel: LoadingViewModel) {
        val products = mutableListOf<ProductDTO>()
        _products.value?.let{products.addAll(it)}

        val apiCall =
            RestUtil.createService(ProductService::class.java, PreferenceManager.getPreference("Token")).changeFavouriteStatus(products[position].id)

        loadingViewModel.changeVisibility(View.VISIBLE)

        apiCall.enqueue(object : Callback<ResponseMessage<ProductDTO>> {
            override fun onResponse(
                call: Call<ResponseMessage<ProductDTO>>,
                response: Response<ResponseMessage<ProductDTO>>
            ) {
                loadingViewModel.changeVisibility(View.GONE)

                if (response.isSuccessful) {

                    if (response.body()?.errorCode != null) {
                        AlertUtil.showAlertMessageForErrorCode(
                            response.body()!!.errorCode,
                            activity
                        )
                    } else {
                        response.body()?.payload?.let { products[position] = it }
                        setProducts(products)
                    }
                }
            }

            override fun onFailure(
                call: Call<ResponseMessage<ProductDTO>>,
                t: Throwable
            ) {

                loadingViewModel.changeVisibility(View.GONE)

                Toast.makeText(
                    Catalogue.application,
                    "Change favourite status failed!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }
}