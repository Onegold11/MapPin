<?php
$conn = mysqli_connect("localhost", "root", "woojin", "locations");
$sql = "SELECT * FROM marker";
$result = mysqli_query($conn, $sql);

if ($result == false) {
    echo mysqli_error($conn);
    error_log(mysqli_error($conn));
}

$result_array = array();
while ($row = mysqli_fetch_array($result)) {
    $column['id'] = htmlspecialchars($row['id']);
    $column['latitude'] = htmlspecialchars($row['latitude']);
    $column['longitude'] = htmlspecialchars($row['longitude']);
    $column['address'] = htmlspecialchars($row['address']);
    $column['created'] = htmlspecialchars($row['created']);
    $column['user'] = htmlspecialchars($row['user']);
    array_push($result_array, $column);
}
echo json_encode(array("result"=>$result_array), JSON_UNESCAPED_UNICODE);
mysqli_close($conn);
?>
