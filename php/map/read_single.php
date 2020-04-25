<?php
$conn = mysqli_connect("localhost", "root", "woojin", "locations");

settype($_GET['id'], 'integer');
$filtered_id = mysqli_real_escape_string($conn, $_GET['id']);
$sql = "
  SELECT * FROM marker
  WHERE id={$filtered_id}
  ";
$result = mysqli_query($conn, $sql);

if ($result == false) {
    echo mysqli_error($conn);
    error_log(mysqli_error($conn));
}

$result_array = array();
$row = mysqli_fetch_array($result);
$result_array['id'] = htmlspecialchars($row['id']);
$result_array['latitude'] = htmlspecialchars($row['latitude']);
$result_array['longitude'] = htmlspecialchars($row['longitude']);
$result_array['address'] = htmlspecialchars($row['address']);
$result_array['created'] = htmlspecialchars($row['created']);
$result_array['user'] = htmlspecialchars($row['user']);

echo json_encode(array("result"=>$result_array), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);
?>
