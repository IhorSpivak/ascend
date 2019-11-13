package com.vrgsoft.carButler.domain.gateway.common.base_gateway

import com.vrgsoft.carButler.domain.gateway.common.BaseGateway
import com.vrgsoft.carButler.domain.use_case.common.NetworkManager

internal class MockedGateway(manager: NetworkManager) : BaseGateway(manager)