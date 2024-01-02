<template>
	<view class="main">
		<view class="u-demo-block">
			<view class="u-demo-block__content">
				<u-tabs :list="list2" itemStyle="height:88rpx;" @change="change">
				</u-tabs>
			</view>
		</view>
		<view class="peadinglist" v-for="(vector,i) in vectorList[index].vec" :key="i">
			<view class="image">
				<image :src="vector.v_imgurl" mode="" class="img"></image>
			</view>
			<view class="content">
				<text style="font-size: 28rpx;" class="v_name">名称：{{vector.v_name}}</text>
				<text v-if="index == 0" style="font-size: 22rpx;" class="v_endtime">发布时间：{{vectorList[index].times[i]}}</text>
				<text v-if="index == 1" style="font-size: 22rpx;" class="v_endtime">结束时间：{{vectorList[index].times[i]}}</text>
				<text v-if="index == 2" style="font-size: 22rpx;" class="v_endtime">结束时间：{{vectorList[index].times[i]}}</text>
			</view>
			<view>
				<view class="btn" style="margin-bottom: 10rpx;" @click="detail(vector)">
					<text>查看</text>
				</view>
				<view class="btn" style="margin-top: 10rpx; border: 2rpx solid red; color: red;"
					@click="delVetor(vector.v_vectorid,i)">
					删除
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
				list2: [{
					name: '进行中',
					badge: {
						value: 0
					}
				}, {
					name: '待处理',
					badge: {
						value: 0
					}
				}, {
					name: '已结束',
					badge: {
						value: 0
					}
				}],
				vectorList: [
					{vec:[],times:[]},{vec:[],times:[]},{vec:[],times:[]}
				],
				index: 0
			}
		},
		onPullDownRefresh() {
			this.load()
		},
		onLoad() {
			this.load();
			//join
			// let num = 0;
			// uni.showLoading({
			// 	title:"加载中..."
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		status:Util.PUBLISH,
			// 		isbin:0
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		num++;
			// 		if(num==3){
			// 			uni.hideLoading()
			// 		}
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.vectorList[0].times)[i] = (Util.dateParse((res.data)[i].v_updatetime))
			// 			res.data[i].v_imgurl = Vue.prototype.urlhead+'/'+res.data[i].v_imgurl
			// 		}
			// 		this.vectorList[0].vec = res.data
			// 		this.list2[0].badge.value = res.data.length
			// 	}
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		status:Util.DEAL,
			// 		isbin:0
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		num++;
			// 		if(num==3){
			// 			uni.hideLoading()
			// 		}
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.vectorList[1].times)[i] = (Util.dateParse((res.data)[i].v_endtime))
			// 			res.data[i].v_imgurl = Vue.prototype.urlhead+'/'+res.data[i].v_imgurl
			// 		}
			// 		this.vectorList[1].vec = res.data
			// 		this.list2[1].badge.value = res.data.length
			// 	}
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		status:Util.END,
			// 		isbin:0
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		num++;
			// 		if(num==3){
			// 			uni.hideLoading()
			// 		}
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.vectorList[2].times)[i] = (Util.dateParse((res.data)[i].v_endtime))
			// 			res.data[i].v_imgurl = Vue.prototype.urlhead+'/'+res.data[i].v_imgurl
			// 		}
			// 		this.vectorList[2].vec = res.data
			// 		this.list2[2].badge.value = res.data.length
			// 	}
			// })
			
		},
		methods: {
			load(){
				let num = 0;
				uni.showLoading({
					title:"加载中..."
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryOwner",
					method: 'POST',
					data: {
						userid: uni.getStorageSync("userid"),
						status:Util.PUBLISH,
						isbin:0
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					timeout:30000,
					success: (res) => {
						num++;
						if(num==3){
							uni.hideLoading()
						}
						console.log(res)
						for (let i in res.data) {
							(this.vectorList[0].times)[i] = (Util.dateParse((res.data)[i].v_updatetime,false))
							res.data[i].v_imgurl = Vue.prototype.urlhead+"/"+res.data[i].v_imgurl
						}
						this.vectorList[0].vec = res.data
						this.list2[0].badge.value = res.data.length
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"错误"
						})
					}
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryOwner",
					method: 'POST',
					data: {
						userid: uni.getStorageSync("userid"),
						status:Util.DEAL,
						isbin:0
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					timeout:30000,
					success: (res) => {
						num++;
						if(num==3){
							uni.hideLoading()
						}
						console.log(res)
						for (let i in res.data) {
							(this.vectorList[1].times)[i] = (Util.dateParse((res.data)[i].v_endtime,false))
							res.data[i].v_imgurl = Vue.prototype.urlhead+"/"+res.data[i].v_imgurl
						}
						this.vectorList[1].vec = res.data
						this.list2[1].badge.value = res.data.length
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"错误"
						})
					}
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryOwner",
					method: 'POST',
					data: {
						userid: uni.getStorageSync("userid"),
						status:Util.END,
						isbin:0
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					timeout:30000,
					success: (res) => {
						num++;
						if(num==3){
							uni.hideLoading()
						}
						console.log(res)
						for (let i in res.data) {
							(this.vectorList[2].times)[i] = (Util.dateParse((res.data)[i].v_endtime,false))
							res.data[i].v_imgurl = Vue.prototype.urlhead+"/"+res.data[i].v_imgurl
						}
						this.vectorList[2].vec = res.data
						this.list2[2].badge.value = res.data.length
					},
					fail:()=>{
						uni.hideLoading()
						uni.showToast({
							icon:"error",
							title:"错误"
						})
					}
				})
				uni.stopPullDownRefresh()
			},
			change(e) {
				this.index = e.index
			},
			detail(vec) {
				uni.setStorageSync("vector", JSON.stringify(vec))
				uni.navigateTo({
					url: "../dealdetail/dealdetail"
				})
			},
			// 删除投票
			delVetor(v_vectorid, i) {
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
										this.vectorList[this.index].vec.splice(i, 1)
										this.list2[this.index].badge.value--
										this.$forceUpdate()
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

<style lang="scss" scoped>
	.u-demo-block__content {
		display: flex;
		justify-content: center;
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
