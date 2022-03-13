package com.yum.stockticker.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yum.stockticker.R
import com.yum.stockticker.StockTickerViewModel
import com.yum.stockticker.data.CompanyType

@Composable
fun StockListDisplay(stockTickerViewModel: StockTickerViewModel) {
    val tickers by stockTickerViewModel.getStockTickers.observeAsState()
    var companyTypeFilterExpanded by remember { mutableStateOf(false) }
    val companyTypeList = CompanyType.values().map(Enum<*>::name)
    var selectedIndex by remember { mutableStateOf(stockTickerViewModel.selectedCompanyTypeIndex) }
    ConstraintLayout {
        val (title, column, item, filterGroup) = createRefs()
        Text(text = stringResource(R.string.stock_list_title),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
        CompanyTypeMenu(companyTypeList, companyTypeFilterExpanded,
            selectedIndex,
            {
                companyTypeFilterExpanded = true
            },
            { index ->
                selectedIndex = index
                stockTickerViewModel.selectedCompanyType = companyTypeList[selectedIndex]
                companyTypeFilterExpanded = false
            },
            {
                companyTypeFilterExpanded = false
            }, modifier = Modifier.constrainAs(filterGroup) {
                top.linkTo(title.bottom)
                start.linkTo(title.start)
            })
        LazyColumn(modifier = Modifier
            .padding(top = 10.dp)
            .constrainAs(column) {
                top.linkTo(filterGroup.bottom)
                start.linkTo(filterGroup.start)
            }) {
            tickers?.let { tcks ->
                items(tcks) { tck ->
                    Row(modifier = Modifier.constrainAs(item) {
                        top.linkTo(column.top)
                        start.linkTo(column.start)
                    }) {
                        Text(text = tck.name)
                        Text(text = tck.price.toString())
                    }
                }
            } ?: run {
                item {
                    Text(text = "None")
                    Text(text = "None")
                }
            }
        }
    }
}

@Composable
fun StockListLayout() {
    var companyTypeFilterExpanded by remember { mutableStateOf(false) }
    val companyTypeList = CompanyType.values().map(Enum<*>::name)

    ConstraintLayout {
        val (title, item, filterGroup) = createRefs()
        Text(text = stringResource(R.string.stock_list_title),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
        CompanyTypeMenu(companyTypeList, companyTypeFilterExpanded, 0,
            {
                companyTypeFilterExpanded = true
            },
            {
                companyTypeFilterExpanded = false
            },
            {
                companyTypeFilterExpanded = false
            }, Modifier.constrainAs(filterGroup) {
                top.linkTo(title.bottom)
                start.linkTo(title.start)
            })
        Row(modifier = Modifier
            .padding(top = 10.dp)
            .constrainAs(item) {
                top.linkTo(filterGroup.bottom)
                start.linkTo(filterGroup.start)
            }) {
            Text(text = "Apple")
            Text(text = "12.222")
        }
    }
}

@Composable
fun CompanyTypeMenu(items: List<String>,
                    expandedState: Boolean,
                    selectedIndex: Int,
                    updateExpandStatus: () -> Unit,
                    onItemClicked: (Int) -> Unit,
                    onDismiss: () -> Unit,
                    modifier: Modifier) {
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
            .padding(top = 10.dp)
            .border(0.5.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
            .clickable(
                onClick = {
                    updateExpandStatus()
                },
            ),

        ) {
        ConstraintLayout {
            val (label, iconView) = createRefs()

            Text(items[selectedIndex], modifier = Modifier.constrainAs(label) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
            Icon(imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = stringResource(R.string.company_type_dropdown_filter_description),
                modifier = Modifier.constrainAs(iconView) {
                    top.linkTo(parent.top)
                    start.linkTo(label.end)
                })
            DropdownMenu(expandedState, onDismissRequest = onDismiss) {
                items.forEachIndexed { index, title ->
                    DropdownMenuItem(onClick = {
                        onItemClicked(index)
                    }) {
                        Text(text = title)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListPreview() {
    StockTickerTheme {
        StockListLayout()
    }
}