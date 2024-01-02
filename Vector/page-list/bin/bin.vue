<template>
	<view>
		<view class="empty" v-if="vectorList.length==0">
			<view class="img">
				<image src="../../static/other/empty_bin.png" mode="aspectFit"></image>
			</view>
			<view class="txt">
				<text>空空如也</text>
			</view>
		</view>
		<view class="main">
			<view class="peadinglist" v-for="(vector,i) in vectorList" :key="i">
				<view class="item" >
					<view class="image">
						<image src="../../static/my/del.png" mode="" class="img"></image>
					</view>
					<view class="content" @click="toggle(i)">
						<text style="font-size: 28rpx;" class="v_name">名称：{{vector.v_name}}</text>
						<text style="font-size: 22rpx;" class="v_endtime">删除时间：{{deltimes[i]}}</text>
					</view>
					<view class="" @click="recovery(vector.v_vectorid,i)">
						<view class="btn" style="margin-bottom: 10rpx;">
							恢复
						</view>
					</view>
				</view>
			</view>
		</view>
		<uni-popup ref="popup_detail" background-color="#fff" type="bottom" v-model="vec_outline">
			<view class="popup_detail">
				<!-- 标题 -->
				<view class="popup_detail_title">
					<text>{{vectorList[vindex].v_name}}</text>
				</view>
				<view class="one">
					<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx; font-size: 28rpx;">描述</view>
					<view class="detail">
						{{vectorList[vindex].v_description}}
					</view>
				</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	import Vue from 'vue'
	import Util from '../../Util.js'
	export default {
		data() {
			return {
				vectorList: [
				// 	{
				// 	v_vectorid: "",
				// 	v_name: "",
				// 	v_updatetime: "",
				// 	v_description: ""
				// },
				],
				deltimes: [],
				vindex: -1
			}
		},
		onPullDownRefresh() {
			this.show()
		},
		onShow() {
			this.show()
			//bin
			// uni.showLoading({
			// 	title:"加载中..."
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		status:-1,
			// 		isbin:1
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		uni.hideLoading()
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.deltimes)[i] = Util.dateParse((res.data)[i].v_updatetime)
			// 		}
			// 		this.vectorList = res.data
			// 	}
			// })
		},
		methods: {
			show(){
				uni.showLoading({
					title:"加载中..."
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryOwner",
					method: 'POST',
					data: {
						userid: uni.getStorageSync("userid"),
						status:-1,
						isbin:1
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					timeout:30000,
					success: (res) => {
						uni.hideLoading()
						uni.stopPullDownRefresh()
						console.log(res)
						for (let i in res.data) {
							(this.deltimes)[i] = Util.dateParse((res.data)[i].v_updatetime,false)
						}
						this.vectorList = res.data
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"错误"
						})
					}
				})
			},
			toggle(vindex) {
				this.$refs.popup_detail.open()
				this.vindex = vindex
			},
			// 恢复投票
			recovery(v_vectorid, index) {
				uni.showModal({
					title: '提示',
					content: '是否恢复？',
					success: (res) => {
						if (res.confirm) {
							uni.showLoading({
								title:"恢复中..."
							})
							console.log('用户点击确定');
							uni.request({
								url: Vue.prototype.urlhead+"/vector/changeStatus",
								method: 'POST',
								header: {
									'content-type': 'application/x-www-form-urlencoded'
								},
								data: {
									vectorid: v_vectorid,
									status: Util.DRAFT
								},
								success: (r) => {
									uni.hideLoading()
									if (r.statusCode == 200) {
										uni.showToast({
											title: '已恢复',
											duration: 1500
										})
										this.vectorList.splice(index, 1)
									}
								}
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
		}
	}
</script>

<style scoped lang="scss">
	.empty {
		.img {
			margin-top: 80rpx;
			display: flex;
			/*设置为flex布局*/
			justify-content: center;
			/*水平居中*/
		}

		.txt {
			text-align: center;
			color: grey;
			font-size: 28rpx;
		}
	}

	.popup_detail {
		height: 350rpx;
		overflow: scroll;
		.popup_detail_title {
			text-align: center;
			margin-top: 20rpx;
		}

		.popup_detail_des {
			width: 95%;
			margin-left: 30rpx;
			margin-right: 30rpx;
			margin-top: 30rpx;

			display: -webkit-box; //将对象作为弹性伸缩盒子模型显示
			-webkit-box-orient: vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）

		}
	}

	.main {
		// background-color: #F6F6F6;
		width: 100%;
		height: 100%;
		overflow-y: scroll;
		padding-left: 20rpx;
		padding-right: 20rpx;
	}

	.item {
		padding-left: 10rpx;
		padding-right: 10rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
		height: 170rpx;
		width: 85%;
		margin-left: 20rpx;
		background-color: #FFFFFF;
		border: 2rpx solid rgba(16, 16, 16, 51);
		border-radius: 100rpx;
		position: relative;
		margin-top: 40rpx;
		margin-bottom: 40rpx;

		.img {
			height: 100rpx;
			width: 100rpx;
		}

		.content {
			display: flex;
			flex-direction: column;
			justify-content: space-around;
			height: 80%;
			// align-items: center;
		}

		.btn {
			width: 120rpx;
			height: 40rpx;
			border-radius: 8rpx;
			background-color: rgba(255, 255, 255, 100);
			color: rgba(42, 143, 247, 100);
			font-size: 20rpx;
			line-height: 40rpx;
			text-align: center;
			border: 2rpx solid rgba(12, 177, 255, 100);
		}
	}
</style>
