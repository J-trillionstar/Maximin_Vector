<template>
	<view class="main">
		<view class="u-demo-block">
			<view class="u-demo-block__content">
				<u-tabs :list="list2" itemStyle="height:88rpx;" @change="change">
				</u-tabs>
			</view>
		</view>
		<!-- 处理列表 -->
		<view class="empty" v-if="vectorList[index].length==0">
			<view class="img">
				<image src="../static/other/empty_deal.png" mode="aspectFit"></image>
			</view>
			<view class="txt">
				<text>暂没有投票等着您</text>
			</view>
		</view>
		<view class="peadinglist" v-for="(vector,i) in vectorList[index].vec" :key="i">
			<view class="image">
				<image :src="vector.v_src" mode="" class="img"></image>
			</view>
			<view class="content">
				<text class="v_name">名称：{{vector.v_name}}</text>
				<text class="v_endtime">截止时间：{{vectorList[index].endtimes[i]}}</text>
				<text class="v_txt" :style="[{color:vector.v_txtcolor}]">{{vector.v_txt}}</text>
			</view>
			<view>
				<view class="btn" @click="enter(vector)">
					进入
				</view>
				<view v-if="vector.v_isend&&index==0" class="btn" style="margin-top: 10rpx; border: 2rpx solid red; color: red;" 
				@click="move(vector)">
					移除
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	import Vue from 'vue'
	import Util from '../Util.js'
	export default {
		data() {
			return {
				list2: [{
					name: '待参与',
					badge: {
						value: 0
					}
				}, {
					name: '待处理',
					badge: {
						value: 0
					}
				}],
				vectorList: [
					{vec:[],endtimes:[]},{vec:[],endtimes:[]}
				],
				index: 0
			}
		},
		onPullDownRefresh() {
			this.show()
		},
		onShow() {
			this.show();
			// uni.showLoading({
			// 	title:"加载中..."
			// })
			// uni.removeStorageSync("people")
			// uni.removeStorageSync("vector")
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryJoinMy",
			// 	method: 'POST',
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		isjoined:false
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res) => {
			// 		uni.showLoading({
			// 			title:"加载中..."
			// 		})
			// 		console.log(res)
			// 		for (let i in res.data) {
			// 			(this.vectorList[0].endtimes)[i] = (Util.dateParse((res.data)[i].v_endtime))
			// 			this.computedTxtStyle((res.data)[i]);
			// 		}
			// 		this.vectorList[0].vec = res.data
			// 		this.list2[0].badge.value = res.data.length
			// 		uni.hideLoading()
			// 	}
			// })
			// uni.request({
			// 	url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 	method: "POST",
			// 	data: {
			// 		userid: uni.getStorageSync("userid"),
			// 		status:Util.DEAL,
			// 		isbin:0
			// 	},
			// 	header: {
			// 		'content-type': 'application/x-www-form-urlencoded'
			// 	},
			// 	success: (res3) => {
			// 		console.log(Util.DEAL)
			// 		console.log(res3)
			// 		for (let i in res3.data) {
			// 			(this.vectorList[1].endtimes)[i] = (Util.dateParse((res3.data)[i].v_endtime))
			// 			this.computedTxtStyle((res3.data)[i]);
			// 		}
			// 		this.vectorList[1].vec = res3.data							
			// 		this.list2[1].badge.value = res3.data.length
			// 		uni.request({
			// 			url: Vue.prototype.urlhead+"/vector/queryOwner",
			// 			method: "POST",
			// 			data: {
			// 				userid: uni.getStorageSync("userid"),
			// 				status:Util.PUBLISH,
			// 				isbin:0
			// 			},
			// 			header: {
			// 				'content-type': 'application/x-www-form-urlencoded'
			// 			},
			// 			success: (res2) => {
			// 				uni.showLoading({
			// 					title:"加载中..."
			// 				})
			// 				console.log(Util.PUBLISH)
			// 				console.log(res2)
			// 				for (let i in res2.data) {
			// 					(this.vectorList[1].endtimes)[i] = (Util.dateParse((res2.data)[i].v_endtime))
			// 					this.computedTxtStyle((res2.data)[i]);
			// 				}
			// 				if (this.vectorList[1].vec.length == 0) {
			// 					this.vectorList[1].vec = res2.data							
			// 				} else {
			// 					this.vectorList[1].vec = this.vectorList[1].vec.concat(res2.data)
			// 				}
			// 				this.list2[1].badge.value += res2.data.length
			// 				uni.hideLoading()
			// 			}
			// 		})
			// 	}
			// })

		},
		methods: {
			move(vec){
				uni.request({
					url:Vue.prototype.urlhead +"/vector/delJoin",
					method:"POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data:{
						userid:uni.getStorageSync("userid"),
						vectorid:vec.v_vectorid
					},
					success(res) {
						console.log(res)
						if(res.statusCode==200){
							uni.showToast({
								title:"移除成功"
							})
							this.$forceUpdate()
						}
					}
				})
			},
			show(){
				uni.showLoading({
					title:"加载中..."
				})
				uni.removeStorageSync("people")
				uni.removeStorageSync("vector")
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryJoinMy",
					method: 'POST',
					data: {
						userid: uni.getStorageSync("userid"),
						isjoined:false
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						uni.showLoading({
							title:"加载中..."
						})
						console.log(res)
						for (let i in res.data) {
							(this.vectorList[0].endtimes)[i] = (Util.dateParse((res.data)[i].v_endtime,false))
							this.computedTxtStyle((res.data)[i],0);
						}
						this.vectorList[0].vec = res.data
						this.list2[0].badge.value = res.data.length
						uni.hideLoading()
					}
				})
				uni.request({
					url: Vue.prototype.urlhead+"/vector/queryOwner",
					method: "POST",
					data: {
						userid: uni.getStorageSync("userid"),
						status:Util.DEAL,
						isbin:0
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res3) => {
						console.log(Util.DEAL)
						console.log(res3)
						var num = 0;
						for (let i in res3.data) {
							(this.vectorList[1].endtimes)[num] = (Util.dateParse((res3.data)[i].v_endtime,false))
							num++;
							this.computedTxtStyle((res3.data)[i],1);
						}
						this.vectorList[1].vec = res3.data							
						this.list2[1].badge.value = res3.data.length
						uni.request({
							url: Vue.prototype.urlhead+"/vector/queryOwner",
							method: "POST",
							data: {
								userid: uni.getStorageSync("userid"),
								status:Util.PUBLISH,
								isbin:0
							},
							header: {
								'content-type': 'application/x-www-form-urlencoded'
							},
							success: (res2) => {
								uni.showLoading({
									title:"加载中..."
								})
								console.log(Util.PUBLISH)
								console.log(res2)
								for (let i in res2.data) {
									(this.vectorList[1].endtimes)[num] = (Util.dateParse((res2.data)[i].v_endtime,false))
									num++
									this.computedTxtStyle((res2.data)[i],1);
								}
								if (this.vectorList[1].vec.length == 0) {
									this.vectorList[1].vec = res2.data							
								} else {
									this.vectorList[1].vec = this.vectorList[1].vec.concat(res2.data)
								}
								this.list2[1].badge.value += res2.data.length
								uni.hideLoading()
							}
						})
					}
				})
				uni.stopPullDownRefresh();
			},
			change(e) {
				this.index = e.index
			},
			computedTxtStyle(vec,flag) {
				var now = Date.parse(new Date());
				var time = Date.parse(new Date(vec.v_endtime));
				var div = parseInt((time - now) / 1000 / 3600);
				if (vec.v_status > 2) {
					vec.v_isend = 1
					if(flag==0){
						vec.v_txt = "已结束"
						vec.v_txtcolor = "grey"
					}else{
						vec.v_txt = "待分配"
						vec.v_txtcolor = "blue"
					}
					vec.v_src = "../static/icon/v_grey.png"
				} else {
					vec.v_txt = "进行中"
					if (div <= 24) {
						vec.v_txtcolor = "red"
						vec.v_src = "../static/icon/v_red.png"
					} else {
						if (div <= 72) {
							vec.v_txtcolor = "orange"
							vec.v_src = "../static/icon/v_yellow.png"
						} else {
							vec.v_txtcolor = "green"
							vec.v_src = "../static/icon/v_green.png"
						}
					}
				}
			},
			enter(vector) {
				uni.setStorageSync("vector", JSON.stringify(vector))
				if (this.index === 0) {
					uni.navigateTo({
						url: "votedetail/votedetail"
					})
				} else {
					console.log("deal")
					uni.navigateTo({
						url: "dealdetail/dealdetail"
					})
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		background-color: #F6F6F6;
		width: 100%;
		height: 100%;
		overflow-y: scroll;
	}

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

	.u-demo-block__content {
		display: flex;
		justify-content: center;
	}

	.peadinglist {
		padding-left: 10rpx;
		padding-right: 10rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
		height: 170rpx;
		width: 90%;
		margin-left: 20rpx;
		background-color: #FFFFFF;
		border: 2rpx solid rgba(16, 16, 16, 51);
		border-radius: 100rpx;
		z-index: 9999;
		position: relative;
		margin-top: 20rpx;
		margin-bottom: 20rpx;

		.img {
			height: 100rpx;
			width: 100rpx;
		}

		.content {
			font-size: 20rpx;
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
