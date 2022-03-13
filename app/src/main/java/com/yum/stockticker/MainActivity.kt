package com.yum.stockticker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yum.stockticker.data.toStockTickers
import com.yum.stockticker.network.WebSocketClient
import com.yum.stockticker.ui.theme.StockListDisplay
import com.yum.stockticker.ui.theme.StockTickerTheme
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : ComponentActivity() {

    private val webSocket = WebSocketClient()
    private val tickerViewModel = StockTickerViewModel()

    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            println("Connected to websocket")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            println("Got a websocket message: $text")
            tickerViewModel.stockTickers.postValue(text.toStockTickers())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockTickerTheme {
                StockListDisplay(tickerViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
    }

    override fun onStop() {
        super.onStop()
        stopWebSocket()
    }

    private fun initWebSocket() {
        webSocket.newWebSocket(Request.Builder().url(STOCKS_WSS).build(), webSocketListener)
    }

    private fun stopWebSocket() {
        webSocket.dispatcher.executorService.shutdown()
    }

    companion object {
        const val STOCKS_WSS = "wss://interviews.yum.dev/ws/stocks"
    }
}