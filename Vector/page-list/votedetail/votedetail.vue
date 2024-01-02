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
			<view class="choose" v-if="(i<4)||(more)" @click="toggle(i)">
				<view class="title" style="font-size: 30rpx;">
					{{can.c_name}}
				</view>
				<view class="main">
					<view class="detail" style="top: -10rpx; position: relative;color: gray;font-size: 24rpx;">
						{{can.c_introduce}}
					</view>
				</view>
				<view class="imglist" style="margin-right: 20rpx;">
					<image :src="can.imgThuList[0]" mode="" style="width: 60rpx; height: 60rpx; margin-right: 20rpx;" >
					</image>
					<image :src="can.imgThuList[1]" mode="" style="width: 60rpx; height: 60rpx;" >
					</image>
				</view>
				<view class="" style="position: absolute; right: 20rpx;" />
			</view>
		</view>
		<view style="margin-right: 60rpx;margin-bottom: 10rpx;">
			<u--text :show="(!more)" type="primary" size="28rpx" align="right" text="显示全部" @click="more = true">
			</u--text>
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
		<!-- 投票 -->
		<view v-if="!(vector.v_isend) && !(isJoined)">
			<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;font-size: 28rpx;">
				<text>决策</text>
				<uni-icons @click="help" type="help-filled" size="30" color="#427BEE" style="margin-left: 550rpx;"></uni-icons>
			</view>
			<view class="vetor">
				<!-- 列表 -->
				<view class="vetor-list" v-for="(res,resindex) in result" :key="resindex">
					<!-- 左侧选项 -->
					<view class="vetor-list-left" @click="toggle_one(resindex)">
						<uni-icons v-if="!(res.l_flag)" type="plusempty" size="25"></uni-icons>
						<view v-for="(one,i) in res.left_checked" :key="i" v-if="(res.l_flag)&&(one)" class="chos">
							{{candidates[i].c_name}}
						</view>
					</view>
					<view class="">
						>>>
					</view>
					<view class="vetor-list-right" @click="toggle_more(resindex)">
						<uni-icons v-if="!(res.r_flag)" type="plusempty" size="25"></uni-icons>
						<view v-for="(one,i) in res.right_checked" :key="i" v-if="(res.r_flag)&&(one)" class="chos">
							{{candidates[i].c_name}}
						</view>
					</view>
				</view>
				<view class="oper" style="margin-left:500rpx;">
					<image class="operimg" src="../../static/other/low.png" mode="aspectFit" @click="lowres"></image>
					<image class="operimg" src="../../static/other/plus.png" mode="aspectFit" @click="addres"></image>
				</view>
			</view>
		</view>

		<view v-if="isJoined">
			<view style="color: gray ; margin-left: 30rpx;margin-top: 30rpx;font-size: 28rpx;">决策</view>
			<view class="vetor">
				<!-- 列表 -->
				<view class="vetor-list" v-for="(res,resindex) in result_db" :key="resindex">
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
		</view>
		<view class="btn">
			<button v-if="!(vector.v_isend) && !(isJoined)" type="primary" class="btn" @click="determine">提交</button>
			<button v-if="vector.v_isend" type="primary" class="btn" style="background-color: #d3d3d3;">已结束</button>
			<button v-if="isJoined" type="primary" class="btn" style="background-color: #d3d3d3;">已参与</button>
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

		<uni-popup ref="popup_onechoice" type="center">
			<view class="popup_choice">
				<u-grid :border="false" col="4" customStyle="padding-top:100rpx;" @click="choosleft">
					<u-grid-item v-for="(item,i) in candidates" :key="i">
						<view class="popup_choice_content_item"
							:style="result[resindex].left_checked[i]?'color:#FFFFFF;background-color: #3388EC;':'color:#3388EC'">
							{{item.c_name}}
						</view>
					</u-grid-item>
				</u-grid>
			</view>
		</uni-popup>
		<uni-popup ref="popup_morechoice" type="center">
			<view class="popup_choice">
				<u-grid :border="false" col="4" customStyle="padding-top:100rpx;" @click="choosright">
					<u-grid-item v-for="(item,i) in candidates" :key="i">
						<view class="popup_choice_content_item"
							:style="result[resindex].right_checked[i]?'color:#FFFFFF;background-color: #3388EC;':'color:#3388EC'">
							{{item.c_name}}
						</view>
					</u-grid-item>
				</u-grid>
			</view>
		</uni-popup>
		
		<uni-popup ref="popup_help" type="center">
			<view class="popup_help">
				<view class="help_text">
					决策中的每一组都是一个序列对，你可以通过下方的加减标志进行序列的添加和删除
				</view>
				<view style="margin-left:250rpx;">
					<image src="../../static/other/help-pd.png" mode="aspectFit" style="width:200rpx; height:50rpx;"></image>
				</view>
				<view class="help_text">
					序列对由左右两个部分
				</view>
				<view class="help_text">
					表示 “左” 优于 “右”
				</view>
				<view class="vetor">
					<!-- 列表 -->
					<view class="vetor-list">
						<!-- 左侧选项 -->
						<view class="vetor-list-left">
							<view class="chos">
								1
							</view>
							<view class="chos">
								2
							</view>
						</view>
						<view class="">
							>>>
						</view>
						<view class="vetor-list-right">
							<view  class="chos">
								3
							</view>
							<view class="chos">
								4
							</view>
						</view>
					</view>
				</view>
				<view class="help_text">
					如上则表示：“1号2号”  优于  “3号4号”
				</view>
				<view class="help_text">
					但是“1号”“2号”之间没有优劣关系，是平等的
				</view>
			</view>
		</uni-popup>
		
		<view v-if="fromsquare">
			<button class="dis" size="mini" plain open-type="share"></button>
			<view class="fab">
				<image class="img" src="../../static/other/share.png" mode="aspectFit" />
				<text style="font-size: 26rpx;display: block;margin-top: -35rpx;">分享</text>
			</view>
		</view>
	</view>
</template>

<script>
	import Util from '../../Util.js'
	import Vue from 'vue'
	export default {
		data() {
			return {
				vector: {
					v_name: "",
					v_endtime: "",
					v_description: ""
				},
				candidates: [
				// 	{
				// 	c_name: "苹果",
				// 	c_introduce: "",
				// 	imgUrlList: [],
				// 	imgThuList: [],
				// 	imgLoad:[]
				// },{c_name:"1"},{c_name:"2"},{c_name:"3"},
				],
				canindex: -1,
				resindex: -1,
				result: [],
				//保留，初始化使用
				checked: [],

				more: false,
				fromsquare: false, //来自广场
				isJoined: false, //已投票

				result_db: []
			};
		},
		onLoad(params) {
			if (uni.getStorageSync("vector")) {
				this.vector = JSON.parse(uni.getStorageSync("vector").split(","))
				this.vector.v_endtime = Util.dateParse(this.vector.v_endtime,true)
				uni.setNavigationBarTitle({
					title: this.vector.v_name
				});
				if(this.vector.islive==1){
					this.judgeRange()
				}
			} else {
				if (params.vectorid) {
					console.log("enterelsevid")
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
							console.log(this.vector)
							uni.setNavigationBarTitle({
								title: this.vector.v_name
							});
							if(this.vector.v_islive==1){
								this.judgeRange()
							}
						}
					})
				}
			}
			uni.request({
				url: Vue.prototype.urlhead+"/vector/queryCandidates",
				method: 'POST',
				data: {
					vectorid: params.vectorid || this.vector.v_vectorid
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
					//初始化数组
					this.result.push({
						l_flag: false,
						r_flag: false,
						left_checked: [].concat(this.checked),
						right_checked: [].concat(this.checked)
					})
				}
			})
			if (params.fromsquare) {
				this.fromsquare = true
			}
			this.isJoin(params.vectorid || this.vector.v_vectorid)
		},
		onShow() {
			if(this.vector.v_islive){
				this.judgeRange()
			}
		},
		onShareAppMessage(res) {
			console.log(this.vector)
			return {
				title: this.vector.v_name,
				imageUrl: this.vector.v_imgurl,
				path: '/pages/home/home?vectorid=' + this.vector.v_vectorid
			}
		},
		methods: {
			loaded(can,i){
				console.log(i)
				can.imgLoad[i] = true
				this.$forceUpdate()
			},
			//预览图片
			viewImage(canindex,i) {
				uni.previewImage({
					current: i,
					urls: this.candidates[canindex].imgUrlList
				})
			},
			//判断现场投票是否在范围内
			judgeRange(){
				let loc = this.vector.v_location.split(",");
				console.log(loc)
				uni.getLocation({
					isHighAccuracy:true,
					success:(res)=> {
						console.log(res.latitude)
						console.log(res.longitude)
						let flag = Util.distance(loc[0],loc[1],res.latitude,res.longitude)
						if(!flag){
							uni.showModal({
								title:"提示",
								content:"您不在投票范围内",
								showCancel:false,
								success: (res) => {
									uni.navigateBack({})
								}
							})
						}
					}
				})
				// let loc = this.vector.v_location.split(",");
				// let flag = Util.distance(loc[0],loc[1]);
				// console.log(flag)
				// if(!flag){
				// 	uni.showModal({
				// 		title:"提示",
				// 		content:"您不在投票范围内",
				// 		showCancel:false,
				// 		success: (res) => {
				// 			uni.navigateBack({})
				// 		}
				// 	})
				// }
			},
			//是否已投票
			isJoin(vectorid) {
				uni.request({
					url: Vue.prototype.urlhead+"/vector/isJoined",
					method: "POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						userid: uni.getStorageSync("userid"),
						vectorid: vectorid
					},
					success: (res) => {
						console.log("isJoin?")
						console.log(res)
						if (res.data.length > 0) {
							this.isJoined = true
							this.result_db = Util.getResultFromDB(res.data[0].j_result)
							console.log(this.result_db)
						} else {
							this.isJoined = false
						}
					}
				})
			},
			help(){
				this.$refs.popup_help.open()
			},
			toggle(canindex) {
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup_detail.open()
				this.canindex = canindex
			},
			toggle_one(resindex) {
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup_onechoice.open()
				this.resindex = resindex
			},
			toggle_more(resindex) {
				// open 方法传入参数 等同在 uni-popup 组件上绑定 type属性
				this.$refs.popup_morechoice.open()
				this.resindex = resindex
			},
			//选择序列左边
			choosleft(index) {
				this.result[this.resindex].l_flag = false;
				this.result[this.resindex].left_checked[index] = !(this.result[this.resindex].left_checked[index])
				for (var i = 0; i < (this.result[this.resindex].left_checked.length); i++) {
					if (this.result[this.resindex].left_checked[i]) {
						this.result[this.resindex].l_flag = true;
						break;
					}
				}
				this.$forceUpdate();
			},
			//选择序列右边
			choosright(index) {
				this.result[this.resindex].r_flag = false;
				this.result[this.resindex].right_checked[index] = !(this.result[this.resindex].right_checked[index])
				for (var i = 0; i < (this.result[this.resindex].right_checked.length); i++) {
					if (this.result[this.resindex].right_checked[i]) {
						this.result[this.resindex].r_flag = true;
						break;
					}
				}
				this.$forceUpdate();
			},
			//增加一个序列
			addres() {
				var restemp = {
					l_flag: false,
					r_flag: false,
					left_checked: [].concat(this.checked),
					right_checked: [].concat(this.checked)
				}
				this.result.push(restemp);
				this.$forceUpdate();
			},
			//减少一个序列
			lowres() {
				this.result.pop();
				this.$forceUpdate();
			},
			//提交结果
			submit() {
				var res_list = [];
				for (var i = 0; i < this.result.length; i++) {
					let left = this.result[i].left_checked;
					let right = this.result[i].right_checked;
					var res = {
						left: [],
						right: []
					}
					for (var j = 0; j < left.length; j++) {
						if (left[j]) {
							(res.left).push(j+1)
						}
					}
					for (var k = 0; k < right.length; k++) {
						if (right[k]) {
							(res.right).push(k+1)
						}
					}
					res_list.push(res)
				}
				if(this.judge(res_list)){
					uni.showLoading({
						title:"提交中..."
					})
					uni.request({
						url: Vue.prototype.urlhead+"/vector/submitResult",
						method: "POST",
						data: {
							vectorid: this.vector.v_vectorid,
							userid: uni.getStorageSync("userid"),
							result: res_list,
							fromsquare: this.fromsquare
						},
						timeout:30000,
						success: (res) => {
							uni.hideLoading()
							if (res.statusCode == 200) {
								if(res.data == "707"){
									uni.showModal({
										title:"提示",
										content:"该投票已结束",
										success: (mod) => {
											if(this.fromsquare){
												uni.switchTab({
													url:"../../pages/square/square"
												})
											}else{
												uni.redirectTo({
													url:"../peading"
												})
											}
										}
									})
								}else{
									uni.showToast({
										title: '提交成功',
										duration: 1000,
										success: () => {
											setTimeout(() => {
												if(this.fromsquare){
													uni.switchTab({
														url:"../../pages/square/square"
													})
												}else{
													uni.redirectTo({
														url:"../peading"
													})
												}
											}, 500)
										}
									})
								}
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
				}else{
					uni.showModal({
						title:"提示",
						content:"投票序列非法"
					})
				}
			},
			judge(res_list){
				console.log(res_list)
				let list = []
				for(let i = 0;i < res_list.length;i++){
					let res = res_list[i]
					for(let k = 0;k<res.left.length;k++){
						for(let j = 0;j<res.right.length;j++){
							let temp = [res.left[k],res.right[j]]
							list.push(temp)
						}
					}
				}
				console.log(list)
				for(let i = 1;i<=this.candidates.length;i++){
					let p =[]
					p.push(i)
					for(let index = 0;index<p.length;index++){
						for(let j = 0;j<list.length;j++){
							if(list[j][0]==p[index]){
								if(i==list[j][1]){
									return false;
								}else{
									p.push(list[j][1])
								}
							}
						}
					}
				}
				return true;
			},
			determine() {
				uni.showModal({
					content: "是否确定提交?",
					cancelText: "否",
					confirmText: "是",
					success: (res) => {
						if (res.confirm) {
							this.submit();
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

	.line {
		width: 92%;
		margin-left: 30rpx;
		margin-right: 30rpx;
		border-bottom: 2rpx solid rgba(187, 187, 187, 100);

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

	.time {
		width: 95%;
		margin-left: 30rpx;
		height: 100rpx;
		display: flex;
		align-items: center;
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

		.oper {
			.operimg {
				display: inline-block;
				height: 60rpx;
				width: 60rpx;
				margin-top: 20rpx;
				margin-right: 20rpx;
				margin-bottom: -40rpx;
			}
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
		margin: 80rpx auto;
	}
	.popup_help{
		background-color: #FFFFFF;
		width: 700rpx;
		height: 800rpx;
		border-radius: 30rpx;
	}
	.popup_choice {
		background-color: #FFFFFF;
		width: 700rpx;
		height: 450rpx;
		border-radius: 30rpx;

		.popup_choice_content_item {
			margin: 20rpx;
			margin-top: 10rpx;
			width: 140rpx;
			height: 50rpx;
			color: #3388EC;
			font-size: 28rpx;
			text-align: center;
			line-height: 50rpx;
			border: 2rpx solid #3388EC;
			border-radius: 20rpx;
		}
	}
	.dis{
		position: fixed;
		bottom: 200rpx;
		left: 30rpx;
		z-index: 99;
		height: 100rpx;
		width: 100rpx;
		border: 0rpx;
	}
	.fab {
		position: fixed;
		bottom: 200rpx;
		left: 30rpx;
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
	.help_text{
		margin: 30rpx;
		font-size: 34rpx;
	}
</style>
