package com.yum.stockticker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yum.stockticker.data.CompanyType
import com.yum.stockticker.data.StockTicker

class StockTickerViewModel: ViewModel() {
    var stockTickers = MutableLiveData<ArrayList<StockTicker>>()

    var selectedCompanyType: String = CompanyType.Any.name

    var selectedCompanyTypeIndex: Int = CompanyType.Any.ordinal

    val getStockTickers = Transformations.map(stockTickers) { tickers ->
        when (selectedCompanyType) {
            CompanyType.Any.name -> tickers
            else -> tickers.filter { it.companyType.contains(selectedCompanyType)}
        }
    }
}