<?php
$conn = mysqli_connect("localhost", "root", "woojin", "locations");

settype($_POST['id'], 'integer');
$filtered = array(
  'id' => mysqli_real_escape_string($conn, $_POST['id'])
);

$sql = "
  DELETE
    FROM marker
    WHERE id = {$filtered['id']}
  ";

$result = mysqli_query($conn, $sql);
if($result == false){
  echo mysqli_error($conn);
  error_log(mysqli_error($conn));
}else{
  echo "delete finish";
}
 ?>
