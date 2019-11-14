package com.doneit.ascend.domain.gateway.common.base_gateway

import com.doneit.ascend.domain.gateway.common.BaseGateway
import com.doneit.ascend.domain.use_case.common.NetworkManager

internal class MockedGateway(manager: NetworkManager) : BaseGateway(manager)