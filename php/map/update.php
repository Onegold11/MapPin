<?php
$conn = mysqli_connect("localhost", "root", "woojin", "locations");

settype($_POST['id'], 'integer');
$filtered = array(
  'id' => mysqli_real_escape_string($conn, $_POST['id']),
  'address' => mysqli_real_escape_string($conn, $_POST['address'])
);

$sql = "
  UPDATE marker
  SET
    address = '{$filtered['address']}'
  WHERE
    id = {$filtered['id']}
  ";

$result = mysqli_query($conn, $sql);
if($result == false){
  echo mysqli_error($conn);
  error_log(mysqli_error($conn));
}else{
  echo "update finish";
}
 ?>
