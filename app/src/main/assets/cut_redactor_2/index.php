<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

	<link href="/templates/gantry/images/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon">
	<link rel="stylesheet" href="/templates/gantry/web-fonts-with-css/css/fontawesome-all.min.css">
	<link rel="stylesheet" href="/sketch/bootstrap.min.css">
	<link rel="stylesheet" href="styles.css">

	<script type="text/javascript" src="/sketch/jquery.min.js"></script>
	<script type="text/javascript" src="/sketch/decimal.js"></script>
	<script type="text/javascript" src="/sketch/paper-full.js"></script>
	<script type="text/javascript" src="/sketch/delaunay.js"></script>
	<script type="text/javascript" src="redactor.js?t=<?php echo time(); ?>"></script>
	
	<title>Редактор раскроя</title>
</head>
<body>
<form method="POST" action="/index.php?option=com_gm_ceiling&view=guild&type=redactor" id="form_redactor" style="display: none;">
	<input type="hidden" name="page" value="cut">
	<input type="hidden" name="ready" value="0" id="ready">
</form>
<div id="preloader" style="display: none;" class="PRELOADER_GM PRELOADER_GM_OPACITY">
    <div class="PRELOADER_BLOCK"></div>
    <img src="/sketch/GM_R_HD.png" class="PRELOADER_IMG">
</div>
<canvas id="myCanvas" resize></canvas>
<div id="panel_cont">
	<div id="panel">
		<button class="sketch_hud btn btn-danger" id ="back" ><i class="fa fa-times" aria-hidden="true"></i></button>
		<button class="sketch_hud btn btn-warning" id ="up" ><i class="fa fa-arrow-up" aria-hidden="true"></i></button>
		<button class="sketch_hud btn btn-warning" id ="down" ><i class="fa fa-arrow-down" aria-hidden="true"></i></button>
		<label id="usadka_label">Усадка:</label><input id="usadka" type="number" step="0.1" min="1" max="30" value="8"><label id="procent_label">%</label>
		<label id="ugol_label">Поворот:</label><input id="ugol" type="number" step="1" min="-1" max="360" value="0"><label id="deg_label">&deg</label>
		<label id="width_label">Ширина:</label><select id="width_polotna"></select>
		<button class="sketch_hud btn btn-warning" id ="plus"><i class="fas fa-redo" aria-hidden="true"></i></button>
		<button class="sketch_hud btn btn-warning" id ="minus"><i class="fas fa-undo" aria-hidden="true"></i></button>
		<!--<button class="sketch_hud btn btn-warning" id ="cut" ><i class="fa fa-cut" aria-hidden="true"></i> <label id="btn_cut_label">Координаты</label></button>-->
		<button id="open_popup" class="sketch_hud btn btn-success"><i class="fa fa-check" aria-hidden="true"></i> <label id="btn_ok_label">Сохранить</label></button>
	</div>
</div>
<div style="display: none;">
	<div style="display: none;">
	<?php
		echo '<script type="text/javascript">';
		echo "var clcid = $calc_id;";
		echo "var prjid = $proj_id;";
		echo "var page = \"$page\";";
		echo 'var walls_points = [];';
		echo 'var diags_points = [];';
		echo 'var pt_points = [];';
		echo 'var code = 64;';
		echo 'var alfavit = 0;';
		echo "var n4 = '$n4';";
		echo "var n5 = '$n5';";
		echo "var n9 = '$n9';";

		echo "var uid = $user_id;";
		echo "var url = \"$url\";";
		echo "var tre = $texture;";
		echo "var col = $color;";
		echo "var man = $manufacturer;";
		echo "var width_polotna = [];";

		if (isset($_POST['width']))
		{
			$width_polotna = $_POST['width'];
			echo "width_polotna = JSON.parse('$width_polotna');";
		}

		if (!empty($_POST['walls']))
		{
			$str = $_POST['walls'];
			$data = explode('||',$str);
			$alphavite = $data[count($data)-1];
			$code = $data[count($data)-2];
			$walls = explode(';',$data[0]);
			array_pop($walls);
			$diags = explode(';',$data[1]);
			array_pop($diags);
			$pt =explode(';',$data[2]);
			array_pop($pt);

			$iter = 0;
			for ($i = 0; $i < count($walls) / 4; $i++)
			{
				$s0_x = $walls[$iter];
				$iter++;
				$s0_y = $walls[$iter];
				$iter++;
				$s1_x = $walls[$iter];
				$iter++;
				$s1_y = $walls[$iter];
				$iter++;
				echo "walls_points[$i] = {s0_x: $s0_x, s0_y: $s0_y, s1_x: $s1_x, s1_y: $s1_y};";
			}

			$iter = 0;
			for ($i = 0; $i < count($diags) / 4; $i++)
			{
				$s0_x = $diags[$iter];
				$iter++;
				$s0_y = $diags[$iter];
				$iter++;
				$s1_x = $diags[$iter];
				$iter++;
				$s1_y = $diags[$iter];
				$iter++;
				echo "diags_points[$i] = {s0_x: $s0_x, s0_y: $s0_y, s1_x: $s1_x, s1_y: $s1_y};";
			}

			$iter = 0;
			for ($i = 0; $i < count($pt) / 2; $i++)
			{
				$x = $pt[$iter];
				$iter++;
				$y = $pt[$iter];
				$iter++;
				echo "pt_points[$i] = {x: $x, y: $y};";
			}

			echo "code = $code;";
			echo "alfavit = $alphavite;";
		}
		echo '</script>';
	?>
	</div>
</div>
</body>
</html>