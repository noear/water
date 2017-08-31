<?php

header( 'Content-Type:text/json;charset=UTF-8');  

function ip() { 
	if (getenv('HTTP_CLIENT_IP')) { 
		$ip = getenv('HTTP_CLIENT_IP'); 
	} 
	elseif (getenv('HTTP_X_FORWARDED_FOR')) { 
		$ip = getenv('HTTP_X_FORWARDED_FOR'); 
	} 
	elseif (getenv('HTTP_X_FORWARDED')) { 
		$ip = getenv('HTTP_X_FORWARDED'); 
	} 
	elseif (getenv('HTTP_FORWARDED_FOR')) { 
		$ip = getenv('HTTP_FORWARDED_FOR'); 
	
	} 
	elseif (getenv('HTTP_FORWARDED')) { 
		$ip = getenv('HTTP_FORWARDED'); 
	} 
	else { 
		$ip = $_SERVER['REMOTE_ADDR']; 
	} 
	return $ip; 
} 	


function sel(){
	$servername = "106.15.45.208";
	$username = "root";
	$password = "gXWTCL18";
	//$dbdatabase = "rock_play";
	
	$tag = $_GET["tag"];
	if(is_null($tag)){
		$tag = "";
	}
	
	
	 
	try{
		// 创建连接
		$conn = new mysqli($servername,  $username, $password);
		 
		// 检测连接
		if ($conn->connect_error) {
		    die("{}");
		} 
		//echo "连接成功";
		
		
		$sql = "SELECT * FROM `water`.config WHERE tag = '" . $tag . "';";
		$result = $conn->query($sql);
	
		if ($result->num_rows > 0) {
			echo "{";
			$idx = 0;
			
		    // 输出每行数据
		    while($row = $result->fetch_assoc()) {
			    if($idx>0){
				    echo ",";
			    }
		        echo '"'.$row["key"].'":{"key":"'.$row["key"].'","url":"'.$row["url"].'","usr":"'.$row["user"].'","pwd":"'.$row["password"].'","exp":"'.$row["explain"].'"}';
		        $idx = $idx+1;
		    }
		    
		    echo "}";
		    
		} else {
		    echo '{}';
		}
		
		$conn->close();
	}
	catch(Exception $e){
		echo '{}';
	}

}

$ipwl = ["127.0.0.1","220.191.47.37",
"106.14.68.99",
"10.31.21.225","10.27.123.48","10.30.40.253","10.30.34.22",
"223.93.139.33","122.224.92.122","122.234.88.155"];
$ip1   = ip();

if(in_array($ip1,$ipwl)){
	sel();
}else{
	echo '{"ip":"'.$ip1.'"}';
}
	

?>