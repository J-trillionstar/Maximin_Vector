<template>
	<view class="main">
		<view class="bc">
		</view>
		<view class="up">
			<view class="search">
				<uni-search-bar @focus="empty" @confirm="query" placeholder="查找决策人..." cancelButton="none" focus v-model="searchValue"></uni-search-bar>
			</view>
			<view style="margin: 19rpx;">
				<button size="mini" style="position: fixed;right: 60rpx;margin-top: 8rpx; width: 150rpx;" @click="init">重置</button>
			</view>
		</view>
		<view class="people">
			<view class="peolist" v-for="(peo,i) in peoples" :key="i">
				<view class="image">
					<image :src="peo.u_imageurl" mode="" class="img"></image>
				</view>
				<view class="name">
					<text style="font-size: 28rpx;position: relative; top: -5rpx;"
						class="v_name">昵称：{{peo.u_username}}</text>
				</view>
				<view class="inputmain">
					<u-cell :border="false">
						<u-number-box slot="right-icon" v-model="peo.j_weight" step="0.1" :min="0"
							:showMinus="peo.j_weight > 0">
							<view slot="minus">
								<image src="../../static/other/low-circle.png" mode="aspectFit"
									style="width: 50rpx;position: relative;top: 5rpx;"></image>
							</view>
							<text slot="input" style="width: 40rpx;text-align: center;"
								class="input">{{peo.j_weight}}</text>
							<view slot="plus">
								<image src="../../static/other/plus-circle-fill.png" mode="aspectFit"
									style="width: 50rpx;position: relative;top: 5rpx;"></image>
							</view>
						</u-number-box>
					</u-cell>
				</view>
			</view>
		</view>
		<view class="btn">
			<button type="primary" class="btn-one" @click="determin">确定</button>
		</view>
	</view>
</template>

<script>
	import Vue from "vue"
	import Util from '../../Util.js'
	export default {
		data() {
			return {
				peoples: [
				// 	{
				// 	u_username: "一万亿颗星",
				// 	u_imageurl: "../../static/my/draft.png",
				// 	j_weight: 1,
				// 	u_userid: ""
				// },
				],
				search: "",
				vectorid: ""
			}
		},
		onLoad(params) {
			if (uni.getStorageSync("people")) {
				this.peoples = JSON.parse(uni.getStorageSync("people").split(","))
			}
			if (params.vectorid) {
				this.vectorid = params.vectorid;
			}
		},
		methods: {
			query(v){
				uni.request({
					url: Vue.prototype.urlhead+"/vector/getPeoLike",
					method: 'POST',
					data: {
						vectorid: this.vectorid,
						username: v.value
					},
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					success: (res) => {
						this.peoples = res.data
					}
				})
			},
			empty(v){
				if(v.value == ""){
					console.log("empty")
					this.peoples = JSON.parse(uni.getStorageSync("people").split(","))
				}
			},
			init() {
				for (let i = 0; i < this.peoples.length; i++) {
					this.peoples[i].j_weight = 1
				}
			},
			determin() {
				uni.showModal({
					content: "确认提交分配的权值?",
					success: (res) => {
						if (res.confirm) {
							uni.showLoading({
								title:"提交中..."
							})
							uni.request({
								url: Vue.prototype.urlhead + "/vector/submitWeight",
								method: "POST",
								data: {
									vectorid: this.vectorid,
									peoples: this.peoples
								},
								timeout:30000,
								success: (suc) => {
									uni.hideLoading()
									if (suc.statusCode == 200) {
										uni.request({
											url: Vue.prototype.urlhead +"/algorithm/maximin",
											method: "POST",
											data: {
												vectorid: this.vectorid
											},
											header: {
												'content-type': 'application/x-www-form-urlencoded'
											},
											success: (suc1) => {
												if (suc1.statusCode == 200) {
													uni.request({
														url: Vue.prototype.urlhead +"/vector/changeStatus",
														method: "POST",
														data: {
															vectorid: this.vectorid,
															status: Util.END //完毕
														},
														header: {
															'content-type': 'application/x-www-form-urlencoded'
														},
														success: (suc2) => {
															if (suc2.statusCode ==200) {
																this.vector =suc2.data
																uni.setStorageSync("vector",JSON.stringify(this.vector))
																uni.redirectTo({
																	url: "../dealdetail/dealdetail"
																})
																uni.removeStorage({
																	key: "peoples"
																})
															}
														}
													})
												}
											}
										})
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
						}
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		width: 100%;
		height: 100%;
		overflow-y: scroll;
	}

	.bc {
		margin-top: 95rpx;
		height: 92%;
		width: 750rpx;
		border-radius: 290rpx;
		background-color: rgba(51, 136, 236, 100);
		position: fixed;
		z-index: -9999;
		opacity: 0.7;
	}

	.up {
		height: 110rpx;
		width: 100%;
		background-color: #FFFFFF;
		border-bottom: solid 0.5rpx grey;
		display: flex;
		position: fixed;
		z-index: 9999;
	}
	/deep/ .uni-searchbar__box{
		width: 450rpx;
		height: 70rpx;
		margin-left: 22rpx;
	}
	.people {
		padding-top: 110rpx;

		.peolist {
			display: flex;
			justify-content: space-around;
			align-items: center;
			height: 100rpx;
			width: 98%;
			margin-left: auto;
			margin-right: auto;
			background-color: #FFFFFF;
			border: 2rpx solid rgba(16, 16, 16, 51);
			border-radius: 56rpx;
			z-index: 99;
			position: relative;
			margin-top: 27rpx;

			.img {
				height: 60rpx;
				width: 60rpx;
			}

			.content {
				display: flex;
				flex-direction: column;
				justify-content: space-around;
				height: 80%;
				// align-items: center;
			}

			.inputmain {
				.input {
					padding: 0 20rpx;
				}

			}
		}
	}

	.btn {
		bottom: 0;
		width: 100%;
		height: 160rpx;
		background-color: #FFFFFF;
		border: solid 0.5rpx grey;
		position: fixed;
		z-index: 9999;

		.btn-one {
			border-radius: 8rpx;
			background-color: #3C9CFF;
			color: rgba(255, 255, 255, 100);
			font-size: 28rpx;
			text-align: center;
			width: 540rpx;
			height: 60rpx;
			line-height: 60rpx;
			top: 55rpx;
			margin-left: 80rpx;
		}
	}
</style>
