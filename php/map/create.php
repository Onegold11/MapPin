<?php
$conn = mysqli_connect("localhost", "root", "woojin", "locations");

$filtered = array(
  'latitude' => mysqli_real_escape_string($conn, $_POST['latitude']),
  'longitude' => mysqli_real_escape_string($conn, $_POST['longitude']),
  'address' => mysqli_real_escape_string($conn, $_POST['address']),
  'user' => mysqli_real_escape_string($conn, $_POST['user'])
);

$sql = "
  INSERT INTO marker
  (latitude, longitude, address, created, user)
  VALUES(
      '{$filtered['latitude']}',
      '{$filtered['longitude']}',
      '{$filtered['address']}',
      NOW(),
      '{$filtered['user']}'
    )";

$result = mysqli_query($conn, $sql);
if($result == false){
  echo mysqli_error($conn);
  error_log(mysqli_error($conn));
}else{
  echo "create finish";
}
 ?>
