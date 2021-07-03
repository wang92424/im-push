/*服务器IP*/
const KIM_HOST = window.location.hostname;
/*
 *服务端 websocket端口
 */
const KIM_PORT = 9090;
const KIM_URI = "ws://" + KIM_HOST + ":" + KIM_PORT;

const APP_VERSION = "1.0.0";
const APP_CHANNEL = "web";
const APP_PACKAGE = "com.kim";

/*
 *特殊的消息类型，代表被服务端强制下线
 */
const ACTION_999 = "999";
const DATA_HEADER_LENGTH = 1;

const MESSAGE = 2;
const REPLY_BODY = 4;
const SENT_BODY = 3;
const PING = 1;
const PONG = 0;
/**
 * PONG字符串转换后
 * @type {Uint8Array}
 */
const PONG_BODY = new Uint8Array([80, 79, 78, 71]);

let bindData = {
    key: "client_bind",
    uid: "",
    channel: "",
    appVersion: "",
    osVersion: "",
    packageName: "",
    deviceId: "",
    deviceName: "",
    timestamp: 0
};

let message = {
    id: 0,
    action: "",
    content: "",
    sender: "",
    receiver: "",
    extra: "",
    title: "",
    format: "",
    timestamp: 0
};

let replyBody = {
    key: "",
    code: "",
    message: "",
    timestamp: 0,
    dataMap: {}
};

let socket;
let manualStop = false;
const KIMPushManager = {};
KIMPushManager.connect = function () {
    manualStop = false;
    window.localStorage.account = '';
    socket = new WebSocket(KIM_URI);
    socket.cookieEnabled = false;
    socket.binaryType = 'arraybuffer';
    socket.onopen = KIMPushManager.innerOnConnectFinished;
    socket.onmessage = KIMPushManager.innerOnMessageReceived;
    socket.onclose = KIMPushManager.innerOnConnectionClosed;
};

KIMPushManager.bind = function (account) {

    window.localStorage.account = account;

    let deviceId = window.localStorage.deviceId;
    if (deviceId === '' || deviceId === undefined) {
        deviceId = generateUUID();
        window.localStorage.deviceId = deviceId;
    }

    let browser = getBrowser();
    bindData.timestamp = new Date().getTime();
    bindData.uid = account;
    bindData.channel = APP_CHANNEL;
    bindData.appVersion = APP_VERSION;
    bindData.osVersion = browser.version;
    bindData.packageName = APP_PACKAGE;
    bindData.deviceId = deviceId;
    bindData.deviceName = browser.name;
    KIMPushManager.sendRequest(bindData);
};

KIMPushManager.stop = function () {
    manualStop = true;
    socket.close();
};

KIMPushManager.resume = function () {
    manualStop = false;
    KIMPushManager.connect();
};


KIMPushManager.innerOnConnectFinished = function () {
    let account = window.localStorage.account;
    if (account === '' || account === undefined) {
        onConnectFinished();
    } else {
        KIMPushManager.bind(account);
    }
};


KIMPushManager.innerOnMessageReceived = function (e) {
    let data = JSON.parse(e.data);
    let type = data.type;
    // let body = data.subarray(DATA_HEADER_LENGTH, data.length);

    if (type === PING) {
        KIMPushManager.pong();
        return;
    }

    if (type === MESSAGE) {
        // let msg = JSON.parse(body, message);
        onInterceptMessageReceived(data);
        return;
    }

    if (type === REPLY_BODY) {
        // let message = JSON.parse(body, replyBody);
        /**
         * 将proto对象转换成json对象，去除无用信息
         */
        let reply = {};
        reply.code = data.code;
        reply.key = data.key;
        reply.message = data.message;
        reply.timestamp = data.timestamp;
        reply.dataMap = {};

        /**
         * 注意，遍历map这里的参数 value在前key在后
         */
        if (data.dataMap) {
            data.dataMap.forEach(function (v, k) {
                reply.dataMap[k] = v;
            });
        }

        onReplyReceived(reply);
    }
};

KIMPushManager.innerOnConnectionClosed = function (e) {
    if (!manualStop) {
        let time = Math.floor(Math.random() * (30 - 15 + 1) + 15);
        setTimeout(function () {
            KIMPushManager.connect();
        }, time);
    }
};

KIMPushManager.sendRequest = function (body) {
    let data = JSON.stringify(body);
    socket.send(data);
};


KIMPushManager.pong = function () {
    let pong = new Map();
    pong.set("type", PONG);
    pong.set("content", 1);
    socket.send(JSON.stringify(pong));
};

function onInterceptMessageReceived(message) {
    /*
     *被强制下线之后，不再继续连接服务端
     */
    if (message.action === ACTION_999) {
        manualStop = true;
    }
    /*
     *收到消息后，将消息发送给页面
     */
    if (onMessageReceived instanceof Function) {
        onMessageReceived(message);
    }
}

function getBrowser() {
    let explorer = window.navigator.userAgent.toLowerCase();
    if (explorer.indexOf("msie") >= 0) {
        let ver = explorer.match(/msie ([\d.]+)/)[1];
        return {name: "IE", version: ver};
    } else if (explorer.indexOf("firefox") >= 0) {
        let ver = explorer.match(/firefox\/([\d.]+)/)[1];
        return {name: "Firefox", version: ver};
    } else if (explorer.indexOf("chrome") >= 0) {
        let ver = explorer.match(/chrome\/([\d.]+)/)[1];
        return {name: "Chrome", version: ver};
    } else if (explorer.indexOf("opera") >= 0) {
        let ver = explorer.match(/opera.([\d.]+)/)[1];
        return {name: "Opera", version: ver};
    } else if (explorer.indexOf("Safari") >= 0) {
        let ver = explorer.match(/version\/([\d.]+)/)[1];
        return {name: "Safari", version: ver};
    }

    return {name: "Other", version: "1.0.0"};
}

function generateUUID() {
    let d = new Date().getTime();
    let uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid.replace(/-/g, '');
}
	 