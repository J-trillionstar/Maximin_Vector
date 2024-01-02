<template>
	<view>
		<view class="empty" v-if="vectorList.length==0">
			<view class="img">
				<image src="../../static/other/empty_join.png" mode="aspectFit"></image>
			</view>
			<view class="txt">
				<text>空空如也</text>
			</view>
		</view>
		<view class="main">
			<view class="peadinglist" v-for="(vector,i) in vectorList" :key="i">
				<view class="image">
					<image :src="vector.v_imgurl" mode="" class="img"></image>
				</view>
				<view class="content">
					<text style="font-size: 28rpx;" class="v_name">名称：{{vector.v_name}}</text>
					<text style="font-size: 22rpx;" class="v_endtime">参加时间：{{jointimes[i]}}</text>
				</view>
				<view>
					<view class="btn" style="margin-bottom: 10px;" @click="detail(vector)">
						查看
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
				vectorList: [],
				jointimes: []
			}
		},
		onPullDownRefresh() {
			this.load()
		},
		onLoad() {
			this.load()
			//join
			// uni.showLoading({
			// 	title:"加载中..."
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryJoinMy",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		isjoined:true
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		uni.hideLoading()
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.endtimes)[i] = Util.dateParse((res.data)[i].v_endtime)
			// 		}
			// 		this.vectorList = res.data
			// 	}
			// })
		},
		methods:{
			load(){
				uni.showLoading({
					title:"加载中..."
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryJoinMy",
					method: 'POST',
					data: {
						userid: uni.getStorageSync("userid"),
						isjoined:true
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
							(this.jointimes)[i] = Util.dateParse((res.data)[i].j_jointime,false)
							res.data[i].v_imgurl = Vue.prototype.urlhead+"/"+res.data[i].v_imgurl
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
			detail(vec){
				uni.setStorageSync("vector", JSON.stringify(vec))
				uni.navigateTo({
					url:"../votedetail/votedetail"
				})
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
