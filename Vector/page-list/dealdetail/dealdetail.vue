<template>
	<view class="conetent">
		<!-- 描述 -->
		<view class="one">
			<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx; font-size: 28rpx;">描述</view>
			<view class="detail">
				{{vector.v_description}}
			</view>
		</view>
		<!--分割线 -->
		<view class="line" style="margin-top: 30rpx;" />

		<!--选项 -->
		<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx; font-size: 28rpx;">选项</view>
		<view v-for="(can,i) in candidates" :key="i">
			<view class="choose" v-if="(i<4)||(more)" @click="toggle_can(i)">
				<view class="title" style="font-size: 30rpx;">
					{{can.c_name}}
				</view>
				<view class="main">
					<view class="detail" style="top: -10rpx; position: relative;color: gray;font-size: 24rpx;">
						{{can.c_introduce}}
					</view>
				</view>
				<view class="imglist">
					<image :src="can.imgThuList[0]" mode=""
						style="width: 60rpx; height: 60rpx; margin-right: 20rpx;">
					</image>
					
					<image :src="can.imgThuList[1]" mode=""
						style="width: 60rpx; height: 60rpx;">
					</image>
					
				</view>
				<uni-icons type="forward" size="20" style="margin-left: 40rpx;"></uni-icons>
				<view class="" style="position: absolute; right: 20rpx;" />
			</view>
		</view>
		<view style="margin-right: 60rpx;margin-bottom: 10rpx;">
			<u--text :show="(!more)" type="primary" size="28rpx" align="right" text="显示全部" @click="more = true"></u--text>
			<u--text :show="(more)" type="primary" size="28rpx" align="right" text="收起" @click="more = false"></u--text>
		</view>
		<!--分割线 -->
		<view class="line" />
		<!-- 时间 -->
		<view class="time">
			<text style="color: gray;font-size: 28rpx;">截止时间</text>
			<u--input inputAlign="right" border="none" v-model="vector.v_endtime" />
		</view>
		<!--分割线 -->
		<view class="line" />

		<view>
			<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;font-size: 28rpx;">
				<text>已决策</text>
				<text style="margin-left: 70%;">人数 {{peoples.length}}</text>
			</view>
			<view v-for="(peo,i) in peoples" :key="i">
				<view class="people" @click="toggle_res(i)" v-if="i>=(scope-1)*4&&i<(scope*4)">
					<image :src="peo.u_imageurl"
						style="width: 60rpx; height: 60rpx; margin-left: 10rpx;">
					</image>
					<view class="main">
						<view class="name">
							{{peo.u_username}}
						</view>
					</view>
					<uni-icons type="forward" size="20" style="margin-left: 40rpx;"></uni-icons>
				</view>
			</view>
			<uni-pagination class="pag" showIcon :total="peoples.length" pageSize="4" title="标题文字" @change="change"/>
		</view>

		<view class="btn">
			<button v-if="vector.v_status==4" type="primary" class="btn" @click="lookresult"
				style="background-color: #F9AE3D">查看结果</button>
			<button v-if="vector.v_status==3" type="primary" class="btn" @click="gotoweight"
				style="background-color: #F9AE3D">分配权值</button>
			<button v-if="vector.v_status==2" type="primary" class="btn" @click="finish"
				style="background-color: #F9AE3D">结束决策</button>
		</view>

		<!-- 普通弹窗 -->
		<uni-popup ref="popup_detail" background-color="#fff" type="bottom" v-model="canindex">
			<view class="popup_detail">
				<!-- 标题 -->
				<view class="popup_detail_title">
					<text>{{candidates[canindex].c_name}}</text>
					<view class="line" style="margin-top: 20rpx;"/>
				</view>
				<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;">描述：</view>
				<view class="popup_detail_des">
					{{candidates[canindex].c_introduce}}
				</view>
				<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;">图片：</view>
				<view class="img" v-for="(imge,i) in candidates[canindex].imgThuList" :key="i">
					<image v-show="candidates[canindex].imgLoad[i]" @load="loaded(candidates[canindex],i)" :src="imge" mode=""
						style="width: 100%;height: 100%;margin-right: 10rpx;" @click="viewImage(canindex,i)"></image>
					<view class="loading" v-if="!candidates[canindex].imgLoad[i]">
						<uni-icons type="spinner-cycle" size="30"></uni-icons>
					</view>
				</view>
			</view>
		</uni-popup>

		<!-- 投票结果弹窗 -->
		<uni-popup ref="res_detail" background-color="#fff" type="bottom" v-model="peoindex">
			<view class="popup_detail">
				<!-- 标题 -->
				<view class="popup_detail_title">
					<text>{{peoples[peoindex].u_username}}</text>
				</view>
				<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;font-size: 28rpx;">决策</view>
				<view class="vetor" v-if="vector.v_status==4 && !(vector.v_isanyo)">
					<!-- 列表 -->
					<view class="vetor-list" v-for="(res,resindex) in peoples[peoindex].j_result" :key="resindex">
						<!-- 左侧选项 -->
						<view class="vetor-list-left">
							<view v-for="(o,i) in res.left" :key="i" class="chos">
								{{candidates[o].c_name}}
							</view>
						</view>
						<view class="">
							>>>
						</view>
						<view class="vetor-list-right">
							<view v-for="(o,i) in res.right" :key="i" class="chos">
								{{candidates[o].c_name}}
							</view>
						</view>
					</view>
				</view>
				<!-- 匿名投票 -->
				<view class="vetor" v-if="vector.v_status==4 && vector.v_isanyo">
					<text>该投票为匿名投票，无法查看</text>
					<button class="btn" @click="gotoweight" style="background-color: #F3A73F;">前往分配</button>
				</view>
				<!-- 未分配权值 -->
				<view class="vetor" v-if="vector.v_status==3">
					<text>您还未为此投票分配权值，无法查看</text>
					<button class="btn" @click="gotoweight" style="background-color: #F3A73F;">前往分配</button>
				</view>
				<!-- 未结束 -->
				<view class="vetor" v-if="vector.v_status == 2">
					<text>该投票尚未结束，无法查看</text>
					<button class="btn" @click="finish" style="background-color: #F3A73F;">结束投票</button>
				</view>
			</view>
		</uni-popup>
		
		<!-- 结果弹窗 -->
		<uni-popup ref="result" background-color="#fff" type="bottom">
			<view class="popup_detail">
				<!-- 标题 -->
				<view class="popup_detail_title">
					<text>投票结果</text>
				</view>
				<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;">NW：</view>
				<view class="result" v-for="(nw,i) in NW" :key="i">
					{{candidates[nw].c_name}}
				</view>
				<view class="rline" />
				<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;">NL：</view>
				<view class="result" v-for="(nl,i) in NL" :key="i">
					{{candidates[nl].c_name}}
				</view>
				<view class="rline" />
				<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;">参考排名：</view>
				<view class="result" v-for="(ref,i) in REF" :key="i">
					{{candidates[ref.can].c_name}} : 决策分数 {{ref.val}} 
				</view>
			</view>
		</uni-popup>
		
		<button class="dis" size="mini" plain open-type="share" v-if="vector.v_status ==2 "></button>
		<view class="fab" :style="vector.v_status==2?'background-color: #427BEE':'background-color: #d4d4d4'">
			<image class="img" src="../../static/other/share.png" mode="aspectFit"/>
			<text style="font-size: 26rpx;display: block;margin-top: -35rpx;">分享</text>
		</view>
	</view>
</template>

<script>
	import Vue from "vue"
	import Util from '../../Util.js'
	export default {
		data() {
			return {
				vector: {
					v_name: "",
					v_endtime: "",
					v_description: "",
					v_status: 2
				},
				candidates: [
				// 	{
				// 	c_name: "",
				// 	imgLoad:[]
				// },
				],
				canindex: -1,
				peoindex: -1,
				peoples: [],
				more: false,
				scope:1,
				
				//结果
				NW:[],
				NL:[],
				REF:[]
			}
		},
		onLoad(params) {
			uni.showLoading({
				title:"加载中"
			})
			if (uni.getStorageSync("vector")) {
				this.vector = JSON.parse(uni.getStorageSync("vector").split(","))
				this.vector.v_endtime = Util.dateParse(this.vector.v_endtime,true)
				uni.setNavigationBarTitle({
					title: this.vector.v_name
				});
			} else {
				if (params.vectorid) {
					uni.request({
						url: Vue.prototype.urlhead+"/vector/queryByParams",
						method: "POST",
						header: {
							'content-type': 'application/x-www-form-urlencoded'
						},
						data: {
							key: "v_vectorid",
							value: params.vectorid
						},
						success: (res) => {
							this.vector = res.data
							uni.setNavigationBarTitle({
								title: this.vector.v_name
							});
						}
					})
				}
			}
			var now = Date.parse(new Date());
			var time = Date.parse(new Date(this.vector.v_endtime));
			var div = parseInt(time - now);
			if (div < 0) {
				this.vector.v_isend = true;
			} else {
				this.vector.v_isend = false;
			}
			uni.request({
				url: Vue.prototype.urlhead+"/vector/queryCandidates",
				method: 'POST',
				data: {
					vectorid: this.vector.v_vectorid || params.vectorid
				},
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				success: (res) => {
					console.log(res)
					let cans = res.data
					for (let j = 0; j < cans.length; j++) {
						cans[j].imgThuList = []
						cans[j].imgLoad = []
						for (let i = 0; i < cans[j].imgUrlList.length; i++) {
							let str = cans[j].imgUrlList[i]
							cans[j].imgUrlList[i] = Vue.prototype.urlhead+"/" + str
							let s = str.split(".")
							cans[j].imgThuList[i] = Vue.prototype.urlhead+"/" + s[0] + Util.Thu + "." + s[1]
							cans[j].imgLoad[i] = false
						}
					}
					this.candidates = cans
				}
			})
			uni.request({
				url: Vue.prototype.urlhead+"/vector/queryJoinPeos",
				method: "POST",
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				data: {
					vectorid: this.vector.v_vectorid || params.vectorid
				},
				success: (res) => {
					console.log(res)
					for (var i = 0; i < res.data.length; i++) {
						res.data[i].j_result = Util.getResultFromDB(res.data[i].j_result)
						if(res.data[i].u_imageurl.indexOf('https')!=0){
							res.data[i].u_imageurl = Vue.prototype.urlhead+'/'+res.data[i].u_imageurl
						}
					}
					this.peoples = res.data
					console.log(this.peoples)
				}
			})
			uni.hideLoading()
			console.log(this.vector)
		},
		onShareAppMessage(res) {
			var vector = uni.getStorageSync("vector")
			return {
				title: vector.v_name,
				imageUrl: vector.v_imgurl,
				path: '/pages/home/home?vectorid='+vector.v_vectorid
			}
		},
		methods: {
			loaded(can,i){
				console.log(i)
				can.imgLoad[i] = true
				this.$forceUpdate()
			},
			viewImage(canindex,i) {
				uni.previewImage({
					current: i,
					urls: this.candidates[canindex].imgUrlList
				})
			},
			toggle_can(canindex) {
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup_detail.open()
				this.canindex = canindex
			},
			toggle_res(peoindex) {
				this.$refs.res_detail.open()
				this.peoindex = peoindex
			},
			gotoweight() {
				uni.setStorageSync("peoples", JSON.stringify(this.peoples))
				uni.redirectTo({
					url: "../weight/weight?vectorid=" + this.vector.v_vectorid
				})
			},
			change(e){
				this.scope = e.current
			},
			lookresult() {
				// this.$refs.result.open()
				// 进结果页面
				uni.request({
					url:  Vue.prototype.urlhead+"/vector/queryByParams",
					method: "POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						key:"v_vectorid",
						value:this.vector.v_vectorid
					},
					success: (res) => {
						console.log(res)
						var wl = res.data.v_result
						var ref = res.data.v_result_reference
						var cal_down = Util.formatResult(wl,ref)
						this.NW = cal_down.NW
						this.NL = cal_down.NL
						this.REF = cal_down.REF
						
						this.$refs.result.open()
					}
				})
			},
			finish() {
				uni.showModal({
					content: "是否提前结束投票",
					success: (res) => {
						if (res.confirm) {
							this.vector.v_isend = true
							//数据库更改
							uni.request({
								url: Vue.prototype.urlhead+"/vector/changeStatus",
								method: "POST",
								header: {
									'content-type': 'application/x-www-form-urlencoded'
								},
								data: {
									vectorid: this.vector.v_vectorid,
									status: Util.DEAL
								},
								success: (suc) => {
									if (suc.statusCode == 200) {
										this.vector = suc.data
									}
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
	.conetent {
		width: 100%;
		height: 100%;

	}
	/deep/ .uni-pagination{
		margin-left: 170rpx;
		margin-right: 170rpx;
	}
	.detail {
		width: 90%;
		font-size: 28rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-top: 30rpx;

		display: -webkit-box; //将对象作为弹性伸缩盒子模型显示
		-webkit-box-orient: vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）

	}

	/deep/.u-input {
		margin-right: 20rpx;
	}

	.choose {
		position: relative;
		width: 95%;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-top: -10rpx;
		height: 100rpx;

		display: flex;
		align-items: center;

		.title{
			width: 100rpx;
			overflow: hidden; //将超出的部分隐藏
			text-overflow: ellipsis; //超出部分用省略号显示
			display: -webkit-box; //将对象作为弹性伸缩盒子模型显示
			-webkit-box-orient: vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）
			-webkit-line-clamp: 1; //这个属性不是css的规范属性，需要组合上面的两个属性，2表示显示的行数(我这里仅写2，可以是任何数字)
		}
		
		.detail {
			width: 300rpx;
			overflow: hidden; //将超出的部分隐藏
			text-overflow: ellipsis; //超出部分用省略号显示
			display: -webkit-box; //将对象作为弹性伸缩盒子模型显示
			-webkit-box-orient: vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）
			-webkit-line-clamp: 2; //这个属性不是css的规范属性，需要组合上面的两个属性，2表示显示的行数(我这里仅写2，可以是任何数字)
			color: gary;
		}
	}

	.line {
		width: 92%;
		margin-left: 30rpx;
		margin-right: 30rpx;
		border-bottom: 2rpx solid rgba(187, 187, 187, 100);

	}

	.time {
		width: 95%;
		margin-left: 30rpx;
		margin-right: 30rpx;

		height: 100rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;

		/deep/.uni-date-x--border {
			border: none;
			text-align: right;
		}

		/deep/.uni-icons {
			display: none;
		}
	}

	.people {
		position: relative;
		width: 95%;
		margin-left: 30rpx;
		margin-right: 30rpx;
		margin-top: -10rpx;
		height: 100rpx;

		display: flex;
		align-items: center;

		.name {
			width: 500rpx;
			position: relative;
			text-align: center;
			font-size: 24rpx;
		}
	}

	.popup_detail {
		margin-top: 40rpx;
		height: 800rpx;
		overflow: scroll;
		.popup_detail_title {
			text-align: center;
			margin-bottom: 20rpx;
		}
		.popup_detail_des {
			width: 95%;
			margin-left: 30rpx;
			margin-right: 30rpx;
			margin-top: 30rpx;

			display: -webkit-box; //将对象作为弹性伸缩盒子模型显示
			-webkit-box-orient: vertical; //从上到下垂直排列子元素（设置伸缩盒子的子元素排列方式）

		}

		.img {
			display: inline-block;
			// width: 95%;
			width: 200rpx;
			height: 200rpx;
			margin-left: 20rpx;
			margin-right: 20rpx;
			margin-top: 20rpx;
		}
		.loading{
			display: inline-block;
			width: 200rpx;
			height: 200rpx;
			text-align: center;
			line-height: 200rpx;
		}
		.vetor {
			width: 95%;
			margin: 30rpx;

			.vetor-list {
				display: flex;
				width: 100%;
				height: 160rpx;
				justify-content: space-around;
				align-items: center;
				margin-top: 20rpx;
				margin-left:-10rpx;

				.vetor-list-left {
					width: 320rpx;
					height: 120rpx;
					border: 2rpx solid rgba(187, 187, 187, 100);
					border-radius: 30rpx;
					display: flex;
					justify-content: center;
					align-items: center;

					.chos {
						width: 100rpx;
						height: 50rpx;
						color: #000000;
						font-size: 28rpx;
						text-align: center;
						line-height: 50rpx;
						border: 2rpx solid #000000;
						border-radius: 20rpx;
					}
				}

				.vetor-list-right {
					width: 320rpx;
					height: 120rpx;
					border: 2rpx solid rgba(187, 187, 187, 100);
					border-radius: 30rpx;
					display: flex;
					justify-content: center;
					align-items: center;

					// padding: 10rpx;
					.chos {
						width: 100rpx;
						height: 50rpx;
						color: #000000;
						font-size: 28rpx;
						text-align: center;
						line-height: 50rpx;
						border: 2rpx solid #000000;
						border-radius: 20rpx;
					}
				}
			}
		}
		.result{
			margin-top:10rpx;
			margin-bottom: 20rpx;
			margin-left: 35rpx;
		}
		.rline{
			width: 80%;
			margin-left: 30rpx;
			margin-right: 30rpx;
			border-bottom: 2rpx solid rgba(187, 187, 187, 100);
		}
	}

	.btn {
		border-radius: 8rpx;
		background-color: rgba(59, 157, 236, 100);
		color: rgba(255, 255, 255, 100);
		font-size: 28rpx;
		text-align: center;
		width: 540rpx;
		height: 60rpx;
		line-height: 60rpx;
		margin: 40rpx auto;
	}

	.dis{
		position: fixed;
		bottom: 200rpx;
		right: 30rpx;
		z-index: 99;
		height: 100rpx;
		width: 100rpx;
		border: 0rpx;
	}
	.fab {
		position: fixed;
		bottom: 200rpx;
		right: 30rpx;
		width: 100rpx;
		height: 100rpx;
		line-height: 100rpx;
		border-radius: 50%;
		text-align: center;
		box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.5);
		background-color: #427BEE;
		
		.img {
			position: relative;
			top: 10rpx;
			height: 70rpx;
			width: 70rpx;
		}
	}
</style>
