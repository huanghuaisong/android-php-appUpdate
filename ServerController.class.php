<?php
namespace API\Controller;
use Think\Controller;
class ServerController extends Controller {
    public function index(){
        //$this->show('<style type="text/css">*{ padding: 0; margin: 0; } div{ padding: 4px 48px;} body{ background: #fff; font-family: "寰蒋闆呴粦"; color: #333;font-size:24px} h1{ font-size: 100px; font-weight: normal; margin-bottom: 12px; } p{ line-height: 1.8em; font-size: 36px }</style><div style="padding: 24px 48px;"> <h1>:)</h1><p>娆㈣繋浣跨敤 <b>ThinkPHP</b>锛?/p><br/>[ 鎮ㄧ幇鍦ㄨ闂殑鏄疕ome妯″潡鐨処ndex鎺у埗鍣?]</div><script type="text/javascript" src="http://tajs.qq.com/stats?sId=9347272" charset="UTF-8"></script>','utf-8');
        echo "hello";
    }

    function updateInfo(){
        $result=array(
            'version'=>'1.3',
            'description'=>'目前已经更新了卡屏功能',
            'url'=>'http://120.27.96.50/mes/index.php/API/Server/download'
        );
        echo json_encode($result);
    }

    function download(){
        $file = "/tmp/test.apk";

        $filename = basename($file);//返回路径中的文件名部分。
        /*
         * header() 函数向客户端发送原始的 HTTP 报头。
         * 必须在任何实际的输出被发送之前调用 header() 函数
         * */
        header("Content-type: application/octet-stream");

//处理中文文件名
        $ua = $_SERVER["HTTP_USER_AGENT"];
        $encoded_filename = rawurlencode($filename);
        if (preg_match("/MSIE/", $ua)) {
            header('Content-Disposition: attachment; filename="' . $encoded_filename . '"');
        } else if (preg_match("/Firefox/", $ua)) {
            header("Content-Disposition: attachment; filename*=\"utf8''" . $filename . '"');
        } else {
            header('Content-Disposition: attachment; filename="' . $filename . '"');
        }

        header("Content-Length: ". filesize($file));

        /*
         * readfile() 函数输出一个文件。
         * 该函数读入一个文件并写入到输出缓冲。
         * 若成功，则返回从文件中读入的字节数。若失败，则返回 false。
         */
        ob_clean();
        flush();
        readfile($file);
    }
}



