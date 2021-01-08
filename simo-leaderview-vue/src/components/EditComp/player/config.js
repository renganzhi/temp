export default {
    "item": {
        "text": "视频流",
        "imgClass": "icon-n-video",
        "chartType": "video",
        "width": 300,
        "height": 300,
        "videoType": "local",
        "videoSrc": "",
        "autoplay": true,
        "loop": true
    },
    "styles": {
        "base": [
            {
                "name": "是否自动播放",
                "key": "autoplay",
                "tag": "select",
                "options": [
                    {
                        "name": "自动播放",
                        "value": true
                    },
                    {
                        "name": "禁止自动播放",
                        "value": false
                    }
                ]
            },
            {
                "name": "是否循环",
                "key": "loop",
                "tag": "select",
                "options": [
                    {
                        "name": "循环",
                        "value": true
                    },
                    {
                        "name": "不循环",
                        "value": false
                    }
                ]
            }
        ]
    }
};
