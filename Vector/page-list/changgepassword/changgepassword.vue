<template>
	<view>
		<view class="form">
			<u--form labelPosition="left" :model="form" ref="r_form" prop="r_form">
				<u-form-item label="旧密码:" prop="old" borderBottom ref="item1" v-if="havepassword">
					<u--input password v-model="form.old" border="none" placeholder="请输入旧密码"></u--input>
				</u-form-item>
				<u-form-item label="新密码:" prop="new" borderBottom ref="item1">
					<u--input password v-model="form.new" border="none" placeholder="请输入新密码"></u--input>
				</u-form-item>
				<u-form-item label="确认密码:" prop="double_new" borderBottom ref="item1">
					<u--input password v-model="form.double_new" border="none" placeholder="请再次输入新密码"></u--input>
				</u-form-item>
			</u--form>
		</view>
		<button type="primary" class="btn-one">确认</button>
	</view>
</template>

<script>
	import Vue from 'vue'
	export default {
		data() {
			return {
				old_password: "",
				havepassword: false,

				form: {
					old: "",
					new: "",
					double_new: ""
				},

				rules: {
					old: [{
						validator: (rule, value, callback) => {
							// 返回true表示校验通过，返回false表示不通过
							return (value == this.old_password);
						},
						message: '旧密码不正确',
						// 触发器可以同时用blur和change
						trigger: 'blur'
					}],
					new: [{
						required: true,
						min: 6,
						max: 15,
						message: '密码长度在 6 ~ 15位之间',
						trigger: 'change'
					}],
					double_new: [{
						validator: (rule, value, callback) => {
							// 返回true表示校验通过，返回false表示不通过
							return (value == this.form.new);
						},
						message: '两次密码不一致',
						// 触发器可以同时用blur和change
						trigger: ['change', 'blur']
					}]
				}
			}
		},
		onReady(){
			this.$refs.r_form.setRules(this.rules)
		},
		onLoad(params) {
			if (params.password) {
				this.havepassword = true
				this.old_password = params.password
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss" scoped>
	.form {
		width: 90%;
		padding-left: 20rpx;
		padding-right: 20rpx;
		margin-top: 20rpx;

		/deep/.u-form-item__body__left__content__label {
			font-size: 28rpx;
			font-weight: bold;
			white-space: nowrap;
			margin-left: 40rpx;
		}

		/deep/.u-input {
			margin-left: 100rpx;
			font-size: 20rpx !important
		}
	}

	.btn-one {
		border-radius: 8rpx;
		background-color: rgba(59, 157, 236, 100);
		color: rgba(255, 255, 255, 100);
		font-size: 26rpx;
		text-align: center;
		width: 540rpx;
		height: 60rpx;
		line-height: 60rpx;
		margin-top: 60rpx;
		margin-left: 50%;
		transform: translateX(-50%);
	}
</style>
