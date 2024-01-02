<template>
	<view>
		<view class="form">
			<u--form labelPosition="left" :model="model1" ref="form1">
				<u-form-item label="手机号" prop="userInfo.name" borderBottom ref="item1">
					<u--input v-model="model1.userInfo.name" border="none" placeholder="请输入手机号"></u--input>
				</u-form-item>
				<view class="item">
					<u-form-item label="验证码" prop="userInfo.name" borderBottom ref="item1" rightIcon="">
						<u--input v-model="model1.userInfo.name" border="none" placeholder="请输入验证码"></u--input>
						<view class="code">
							获取验证码
						</view>
					</u-form-item>
				</view>

			</u--form>
		</view>
		<button type="primary" class="btn-one">确认</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.code {
		position: absolute;
		right: 0;
		top: 50%;
		transform: translateY(-50%);
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

	.item {
		position: relative;
	}

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
		font-size: 24rpx;
		text-align: center;
		width: 540rpx;
		height: 60rpx;
		line-height: 60rpx;
		margin-top: 60rpx;
		margin-left: 50%;
		transform: translateX(-50%);
	}
</style>
