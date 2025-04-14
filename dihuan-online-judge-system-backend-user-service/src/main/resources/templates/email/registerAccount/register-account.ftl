<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>密码重置验证码</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+SC:wght@300;400;500;700&display=swap');

        body {
            font-family: 'Noto Sans SC', Arial, sans-serif;
            line-height: 1.6;
            color: #333333;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
        }

        .card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }

        .header {
            background: linear-gradient(135deg, #3b82f6, #2563eb);
            /* color: white; */
            padding: 30px 20px 10px 20px;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .logo {
            font-size: 24px;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .logo, .header h1 {
            position: relative;
            z-index: 1; /* 确保文字在图片上层 */
        }

        .header img {
            position: absolute;
            top: 0;
            left: 0;
            /* width: 100%; */
            height: 100%;
            z-index: 0;
            opacity: 0.7; /* 调整背景图透明度 */
        }

        .verification-code {
            font-size: 32px;
            font-weight: 700;
            letter-spacing: 8px;
            color: #2563eb;
            background-color: #f0f7ff;
            padding: 15px 20px;
            border-radius: 8px;
            display: inline-block;
            margin: 20px 0;
        }

        .content {
            padding: 30px;
        }

        .footer {
            padding: 20px;
            text-align: center;
            color: #666666;
            font-size: 14px;
            border-top: 1px solid #eeeeee;
        }

        .text-center {
            text-align: center;
        }

        .mb-4 {
            margin-bottom: 1rem;
        }

        .mb-6 {
            margin-bottom: 1.5rem;
        }

        .mt-6 {
            margin-top: 1.5rem;
        }

        .mt-2 {
            margin-top: 0.5rem;
        }

        .text-sm {
            font-size: 0.875rem;
        }

        .text-xs {
            font-size: 0.75rem;
        }

        .text-gray-500 {
            color: #6b7280;
        }

        .text-gray-400 {
            color: #9ca3af;
        }

        .text-blue-500 {
            color: #3b82f6;
        }

        .font-semibold {
            font-weight: 600;
        }

        @media only screen and (max-width: 600px) {
            .container {
                padding: 10px;
            }

            .header {
                padding: 20px 15px;
            }

            .verification-code {
                font-size: 24px;
                letter-spacing: 5px;
                padding: 12px 15px;
            }

            .content {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <div class="header">
            <img src="cid:dihuanImage" alt="">
            <div class="logo">迪幻OJ在线判题系统</div>
            <h1 style="font-size: 1.5rem; font-weight: 700;">账号注册请求</h1>
        </div>

        <div class="content">
            <p class="mb-4">尊敬的用户，您好：</p>

            <p class="mb-6">我们收到了您注册迪幻OJ在线判题系统的请求。请使用以下验证码继续操作：</p>

            <div class="text-center">
                <div class="verification-code">${verificationCode}</div>
            </div>

            <p class="mb-6 text-sm text-gray-500">此验证码将在<span class="font-semibold">${ttl}分钟</span>后失效。如果您没有请求注册迪幻OJ在线判题系统，请忽略此邮件或联系客服。</p>

            <p class="mt-6 text-sm text-gray-500">温馨提示：请勿将验证码透露给他人，包括客服人员。我们不会通过电话或短信索要验证码。</p>
        </div>

        <div class="footer">
            <p>© 2025 迪幻OJ在线判题系统 版权所有</p>
            <p class="mt-2 text-xs text-gray-400">
                您收到此邮件是因为您申请了注册迪幻OJ在线判题系统。
                <br>
                如有任何疑问，请联系 <a href="mailto:449134710@qq.com" class="text-blue-500">449134710@qq.com</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>