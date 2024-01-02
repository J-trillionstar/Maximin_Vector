<template>
	<view>
		<view class="empty" v-if="vectorList.length==0">
			<view class="img">
				<image src="../../static/other/empty_draft.png" mode="aspectFit"></image>
			</view>
			<view class="txt">
				<text>空空如也</text>
			</view>
		</view>
		<view class="main">
			<view class="peadinglist" v-for="(vector,i) in vectorList" :key="i">
				<view class="image">
					<image src="../../static/my/draft.png" mode="" class="img"></image>
				</view>
				<view class="content">
					<text style="font-size: 28rpx;" class="v_name">名称：{{vector.v_name}}</text>
					<text style="font-size: 22rpx;" class="v_endtime">修改时间：{{updatetimes[i]}}</text>
				</view>
				<view>
					<view class="btn" style="margin-bottom: 10rpx;" @click="gotoEdit(vector)">
						编辑
					</view>
					<view class="btn" style="margin-top: 10rpx; border: 2rpx solid red; color: red;"
						@click="delVetor(vector.v_vectorid,i)">
						删除
					</view>
				</view>
			</view>
		</view>
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
				// 	v_updatetime: ""
				// },
				],
				updatetimes: []
			}
		},
		onPullDownRefresh() {
			this.show();
		},
		onShow() {
			this.show();
			//draft
			// uni.showLoading({
			// 	title:"加载中..."
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		status:Util.DRAFT,
			// 		isbin:0
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		uni.hideLoading()
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.updatetimes)[i] = Util.dateParse((res.data)[i].v_updatetime)
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
						status:Util.DRAFT,
						isbin:0
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
							(this.updatetimes)[i] = Util.dateParse((res.data)[i].v_updatetime,false)
						}
						this.vectorList = res.data
						console.log(res.data)
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
			gotoEdit(vector) {
				uni.setStorageSync("vector", JSON.stringify(vector))
				uni.navigateTo({
					url: "../addvote?&fromdraft=1"
				})
			},
			// 删除投票
			delVetor(v_vectorid, index) {
				uni.showModal({
					title: '提示',
					content: '是否删除？',
					success: (res) => {
						if (res.confirm) {
							uni.showLoading({
								title:"删除中..."
							})
							console.log('用户点击确定');
							uni.request({
								url: Vue.prototype.urlhead+"/vector/changeBin",
								method: 'POST',
								header: {
									'content-type': 'application/x-www-form-urlencoded'
								},
								data: {
									vectorid: v_vectorid,
									v_isbin:1
								},
								timeout:30000,
								success: (r) => {
									uni.hideLoading()
									if (r.statusCode == 200) {
										uni.showToast({
											title: '已删除',
											duration: 1000
										})
										this.vectorList.splice(index, 1)
									}
								},
								fail:()=>{
									uni.hideLoading()
									uni.showToast({
										icon:"error",
										title:"错误"
									})
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
	.empty{
		.img {
			margin-top: 80rpx;
			display: flex;
			/*设置为flex布局*/
			justify-content: center;
			/*水平居中*/
		}
		
		.txt {
			text-align: center;
			color:grey;
			font-size: 28rpx;
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


	.peadinglist {
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
		z-index: 9999;
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
