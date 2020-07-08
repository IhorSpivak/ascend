package com.doneit.ascend.domain.gateway.gateway.community_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.domain.use_case.PaginationDataSource
import com.doneit.ascend.domain.use_case.PaginationSourceLocal
import com.doneit.ascend.domain.use_case.PaginationSourceRemote
import com.doneit.ascend.domain.use_case.gateway.ICommunityFeedGateway
import com.doneit.ascend.source.storage.remote.repository.community_feed.ICommunityFeedRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.doneit.ascend.source.storage.local.repository.community_feed.ICommunityFeedRepository as ILocalCommunityFeedRepository

class CommunityFeedGateway(
    networkManager: NetworkManager,
    private val communityRemote: ICommunityFeedRepository,
    private val communityLocal: ILocalCommunityFeedRepository
) : BaseGateway(networkManager), ICommunityFeedGateway {

    override fun loadPosts(
        scope: CoroutineScope,
        communityFeedDTO: CommunityFeedDTO
    ): LiveData<PagedList<Post>> = liveData {
        emitSource(
            PaginationDataSource.Builder<Post>()
                .coroutineScope(scope)
                .localSource(object : PaginationSourceLocal<Post> {
                    override suspend fun loadData(page: Int, limit: Int): List<Post>? {
                        return communityLocal.getFeed(page * limit, limit).map { it.toEntity() }
                    }

                    override suspend fun save(data: List<Post>) {
                        communityLocal.insertFeed(data.map { it.toLocal() })
                    }
                })
                .pageLimit(communityFeedDTO.perPage ?: 10)
                .remoteSource(object : PaginationSourceRemote<Post> {
                    override suspend fun loadData(page: Int, limit: Int): List<Post>? {
                        return communityRemote.loadPosts(communityFeedDTO.toRequest(page))
                            .successModel
                            ?.posts
                            ?.map { it.toEntity() }
                    }
                })
                .build()
        )
    }

    //TODO remove mock
    override fun loadChannels(
        scope: CoroutineScope,
        communityFeedDTO: CommunityFeedDTO
    ): LiveData<PagedList<Channel>> = liveData {
        emitSource(
            PaginationDataSource.Builder<Channel>()
                .coroutineScope(scope)
                .localSource(object : PaginationSourceLocal<Channel> {
                    override suspend fun loadData(page: Int, limit: Int): List<Channel>? {
                        return listOf(
                            Channel(
                                1,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                2,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                3,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                4,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                5,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            )
                        )
                    }

                    override suspend fun save(data: List<Channel>) {

                    }
                })
                .pageLimit(communityFeedDTO.perPage ?: 10)
                .remoteSource(object : PaginationSourceRemote<Channel> {
                    override suspend fun loadData(page: Int, limit: Int): List<Channel>? {
                        return return listOf(
                            Channel(
                                1,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                2,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                3,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                4,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            ),
                            Channel(
                                5,
                                "https://image.freepik.com/free-photo/image-human-brain_99433-298.jpg",
                                "Title Example"
                            )
                        )
                    }
                })
                .build()
        )
    }

    override fun likePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            val response = communityRemote.likePost(postId)
            if (response.isSuccessful) {
                communityLocal.updatePost(response.successModel!!.toEntity().toLocal())
                baseCallback.onSuccess(Unit)
            } else {
                baseCallback.onError(response.message)
            }
        }
    }

    override fun unlikePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            val response = communityRemote.unlikePost(postId)
            if (response.isSuccessful) {
                communityLocal.updatePost(response.successModel!!.toEntity().toLocal())
                baseCallback.onSuccess(Unit)
            } else {
                baseCallback.onError(response.message)
            }
        }
    }

    override fun createPostComment(
        coroutineScope: CoroutineScope,
        postId: Long,
        postComment: String,
        baseCallback: BaseCallback<Unit>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            val response = communityRemote.leaveComment(postId, postComment)
            if (response.isSuccessful) {
                //TODO insert comment locally
                baseCallback.onSuccess(Unit)
            } else {
                baseCallback.onError(response.message)
            }
        }
    }
}