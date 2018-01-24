jQuery(document).ready(function()
{
var reg_url = /gm-vrn/;
if (!reg_url.test(location.href))
{
	window.history.back();
}

if (typeof(width_polotna) === "undefined" || typeof(diup) === "undefined" || typeof(rup) === "undefined" || typeof(tre) === "undefined" 
	|| typeof(col) === "undefined" || typeof(man) === "undefined" || typeof(title) === "undefined")
{
	window.history.back();
}

paper.install(window);
paper.setup("myCanvas");
var tool = new Tool();
resize_canvas();
jQuery(window).resize(resize_canvas);

var elem_newLength, elem_window, elem_useGrid, elem_jform_n4, elem_jform_n5, elem_jform_n9, elem_preloader, elem_button_ok, elem_nums = [];
var start_point, end_point, move_point, begin_point, c_point;
var point_start_or_end;
var krug_start, krug_end;
var line_h, line_v;
var lines = [];
var lines_sort =[];
var chert_close = false;
var line_bad = false;
var opred_rez;
var cansel_array = [];
var count_cansel = 0;
var text_lines = [];
var text_diag = [];
var text_v, text_h;
var text_points = [];
var g_points = [];
var diag = [];
var fixed_walls = [];
var razv_wall = [];
var razv_wall_kos = [];
var coef_wall_kos = [];
var triangles = [];
var fixed_diags = [];
var vh = 'v';
var points;
var code = 64, alfavit = 0;
var timer1, timer_mig, timer_button_color, timer_url;
var fix_point_dvig;
var touch1, touch2;
var ready = false;
var text_points_lines_id = [];
var down_g, right_g, up_g, left_g, granica;
var diag_sort = [];
var width_final, square_obrezkov = 0, angle_final;
clicks();
///////////////////////
Function.prototype.process= function(state){
    var process= function(){
        var args= arguments;
        var self= arguments.callee;
        setTimeout(function(){
            self.handler.apply(self, args);
        }, 0);
    };
    for(var i in state)
	{
		process[i]= state[i];
	}
    process.handler= this;
    return process;
};

project.activeLayer.applyMatrix = false;
jQuery('#myCanvas').css('resize', 'both');

function resize_canvas()
{
	jQuery("#myCanvas").css("width", document.body.clientWidth - 20 );
	jQuery("#myCanvas").css("height", document.body.clientHeight - 120 );
	view.viewSize = new Size(document.body.clientWidth - 20, document.body.clientHeight - 120);
	view.center = new Point(Math.round(view.viewSize.width / 2), Math.round(view.viewSize.height / 2));

	sdvig();

	zoom(1);
}

document.getElementById('back').onclick = function()
{
    AndroidFunction.func_back(1);
};

document.getElementById('open_popup').onclick = function()
{
	if (!ready)
	{
		return;
	}
	jQuery("#popup").css("display", "block");
};

document.getElementById('close_popup').onclick = function()
{
	jQuery("#popup").css("display", "none");
};

var close_sketch_click_bool = false, walls_points = [], diags_points = [], pt_points = [];

function close_sketch_click_all()
{
	close_sketch_click = close_sketch_click.process();
	elem_preloader.style.display = 'block';
	document.getElementById('popup').style.display = 'none';
	setTimeout(close_sketch_click, 200);
}

var close_sketch_click = function()
{
	if (close_sketch_click_bool)
	{
		return;
	}
	if (!ready)
	{
		return;
	}
	close_sketch_click_bool = true;

	var l1, l2, lc, lines_length = [];
	for (var i = 0; i < lines_sort.length; i++)
	{
		l1 = "";
		l2 = "";
		for (var j = text_points.length; j--;)
		{
			if (point_ravny(new Point(lines_sort[i].segments[0].point.x - 10, lines_sort[i].segments[0].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(lines_sort[i].segments[0].point.x + 10, lines_sort[i].segments[0].point.y - 5), text_points[j].point))
			{
				l1 = text_points[j].content;
			}
			if (point_ravny(new Point(lines_sort[i].segments[1].point.x - 10, lines_sort[i].segments[1].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(lines_sort[i].segments[1].point.x + 10, lines_sort[i].segments[1].point.y - 5), text_points[j].point))
			{
				l2 = text_points[j].content;
			}
		}
		if (l1 > l2)
		{
			lc = l1;
			l1 = l2;
			l2 = lc;
		}
		lines_length.push({name: l1 + l2, length: Math.round(lines_sort[i].length)});
	}
	for (var i in diag)
	{
		l1 = "";
		l2 = "";
		for (var j = text_points.length; j--;)
		{
			if (point_ravny(new Point(diag[i].segments[0].point.x - 10, diag[i].segments[0].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(diag[i].segments[0].point.x + 10, diag[i].segments[0].point.y - 5), text_points[j].point))
			{
				l1 = text_points[j].content;
			}
			if (point_ravny(new Point(diag[i].segments[1].point.x - 10, diag[i].segments[1].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(diag[i].segments[1].point.x + 10, diag[i].segments[1].point.y - 5), text_points[j].point))
			{
				l2 = text_points[j].content;
			}
		}
		if (l1 > l2)
		{
			lc = l1;
			l1 = l2;
			l2 = lc;
		}
		lines_length.push({name: l1+l2, length: Math.round(diag[i].length)});
	}

	var tre = fun_tre.getTre();

	if (tre === 29)
	{
        save_calc_image();
        lines_length1 = JSON.stringify(lines_length);
        AndroidFunction.func_elem_jform_ll(lines_length1);
		zerkalo(1);
	}
	else
	{

		zerkalo(0.92);
	}

	for (var key = lines_sort.length; key--;)
	{
		walls_points[key] = {s0_x: lines_sort[key].segments[0].point.x, s0_y: lines_sort[key].segments[0].point.y,
						s1_x: lines_sort[key].segments[1].point.x, s1_y: lines_sort[key].segments[1].point.y};
	}
	for (var key = diag_sort.length; key--;)
	{
		diags_points[key] = {s0_x: diag_sort[key].segments[0].point.x, s0_y: diag_sort[key].segments[0].point.y,
						s1_x: diag_sort[key].segments[1].point.x, s1_y: diag_sort[key].segments[1].point.y};
	}
	for (var key = text_points.length; key--;)
	{
		pt_points[key] = {x: text_points[key].point.x, y: text_points[key].point.y};
	}
	get_original_sketch();
	
	if (tre === 29)
	{
		
		tkan();
		save_cut_img();
	}
	else
	{
		polotno();

	    save_cut_img();

	    remove_none_shvy();
	    rotate_final();
	    zerkalo(1);
	    remove_pt_intersects();

        save_calc_image();

        lines_length1 = JSON.stringify(lines_length);
        AndroidFunction.func_elem_jform_ll(lines_length1);
	}
	timer_url = setInterval(change_window_location, 200);
	elem_preloader.style.display = 'none';
	close_sketch_click_bool = false;

	AndroidFunction.func_back(1);
};

function change_window_location()
{
	if (suc_a1 && suc_a2 && suc_a3)
	{
		window.location = rup;
		clearInterval(timer_url);
	}
}

var suc_a1 = false, suc_a2 = false, suc_a3 = false;

function w_h(){

    if (document.body.clientWidth > document.body.clientHeight)
	{
		jQuery("#myCanvas").css("width", document.body.clientHeight);
		jQuery("#myCanvas").css("height", document.body.clientHeight - 120);
		view.viewSize = new Size(document.body.clientHeight, document.body.clientHeight - 120);
	}
	else if (document.body.clientWidth < document.body.clientHeight)
	{
		jQuery("#myCanvas").css("width", document.body.clientWidth - 20);
		jQuery("#myCanvas").css("height", document.body.clientWidth);
		view.viewSize = new Size(document.body.clientWidth - 20, document.body.clientWidth);
	}

}
function save_calc_image(){

    w_h();

	text_points_sdvig();
	zoom(2);
	align_center();
	zoom(1);
	
	view.update();

    var MainCanvas = document.getElementById('myCanvas');
	var hidden_canv = document.createElement('canvas');
        hidden_canv.style.display = 'none';
        document.body.appendChild(hidden_canv);
        hidden_canv.width = MainCanvas.width;
        hidden_canv.height = MainCanvas.height;
        var hidden_ctx = hidden_canv.getContext('2d');
        hidden_ctx.drawImage(MainCanvas, 0, 0,  hidden_canv.width,  hidden_canv.height, 0, 0,
        	hidden_canv.width, hidden_canv.height);
        var image = hidden_canv.toDataURL("screenshot/png");

    AndroidFunction.func_elem_jform_n4(elem_jform_n4.value);
    AndroidFunction.func_elem_jform_n5(elem_jform_n5.value);
    AndroidFunction.func_elem_jform_n9(elem_jform_n9.value);
    AndroidFunction.func_elem_jform_image(image);
}

function save_cut_img()
{
    w_h();

	text_points_sdvig();
	zoom(2);
	align_center();
	zoom(1);
	
	view.update();
    var MainCanvas = document.getElementById('myCanvas');

    var hidden_canv = document.createElement('canvas');
        hidden_canv.style.display = 'none';
        document.body.appendChild(hidden_canv);
        hidden_canv.width = MainCanvas.width;
        hidden_canv.height = MainCanvas.height;
        var hidden_ctx = hidden_canv.getContext('2d');
        hidden_ctx.drawImage(MainCanvas, 0, 0,  hidden_canv.width,  hidden_canv.height, 0, 0, hidden_canv.width, hidden_canv.height);
        var image = hidden_canv.toDataURL("screenshot/png");

    AndroidFunction.func_elem_jform_square_obrezkov(square_obrezkov);
    AndroidFunction.func_elem_jform_image_cut(image);

    js_polotna = JSON.stringify (koordinats_poloten);
    AndroidFunction.func_elem_jform_koordinats_poloten(js_polotna);

    elem_preloader.style.display = 'none';
}

function get_original_sketch()
{

    js_polotna = JSON.stringify(walls_points);
    AndroidFunction.func_elem_jform_walls_points(js_polotna);

    js_polotna = JSON.stringify(diags_points);
    AndroidFunction.func_elem_jform_diags_points(js_polotna);

    js_polotna = JSON.stringify(pt_points);
    AndroidFunction.func_elem_jform_pt_points(js_polotna);

    AndroidFunction.func_elem_jform_code(code);

    AndroidFunction.func_elem_jform_alfavit(alfavit);
}

function sdvig()
{
	if (chert_close)
	{
		var sx, sy;
		add_granica();

		for (var key in lines)
		{
			lines[key].segments[0].point.x = parseFloat(lines[key].segments[0].point.x.toFixed(2));
			lines[key].segments[1].point.x = parseFloat(lines[key].segments[1].point.x.toFixed(2));
			lines[key].segments[0].point.y = parseFloat(lines[key].segments[0].point.y.toFixed(2));
			lines[key].segments[1].point.y = parseFloat(lines[key].segments[1].point.y.toFixed(2));
		}
		g_points = getPathsPoints(lines);
		var points_m = findMinAndMaxCordinate_g();
		
		if (points_m.maxY > down_g.position.y)
		{
			sy = -Math.round(Decimal.abs(new Decimal(points_m.maxY).minus(down_g.position.y)).toNumber());
			//console.log('↓');

			sdvig_sy(sy);
		}

		if (points_m.maxX > right_g.position.x)
		{
			sx = -Math.round(Decimal.abs(new Decimal(points_m.maxX).minus(right_g.position.x)).toNumber());
			//console.log('→');

			sdvig_sx(sx);
		}

		if (points_m.minX < left_g.position.x)
		{
			sx = Math.round(Decimal.abs(new Decimal(points_m.minX).minus(left_g.position.x)).toNumber());
			//console.log('←');

			sdvig_sx(sx);
		}

		if (points_m.minY < up_g.position.y)
		{
			sy = Math.round(Decimal.abs(new Decimal(points_m.minY).minus(up_g.position.y)).toNumber());
			//console.log('↑');

			sdvig_sy(sy);
		}
		clear_granica();
		for (var key in lines)
		{
			lines[key].segments[0].point.x = parseFloat(lines[key].segments[0].point.x.toFixed(2));
			lines[key].segments[1].point.x = parseFloat(lines[key].segments[1].point.x.toFixed(2));
			lines[key].segments[0].point.y = parseFloat(lines[key].segments[0].point.y.toFixed(2));
			lines[key].segments[1].point.y = parseFloat(lines[key].segments[1].point.y.toFixed(2));
		}
		g_points = getPathsPoints(lines);
	}
}

function sdvig_sy(sy)
{
	for (var key in project.activeLayer.children)
	{
		project.activeLayer.children[key].position.y = new Decimal(project.activeLayer.children[key].position.y).plus(sy).toNumber();
	}

	for (var key in cansel_array)
	{
		if (cansel_array[key].vid === '1')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].y = new Decimal(cansel_array[key].line_id[i][0].y).plus(sy).toNumber();
					cansel_array[key].line_id[i][1].y = new Decimal(cansel_array[key].line_id[i][1].y).plus(sy).toNumber();
				}
			}
			cansel_array[key].start_point.y = new Decimal(cansel_array[key].start_point.y).plus(sy).toNumber();
			cansel_array[key].end_point.y = new Decimal(cansel_array[key].end_point.y).plus(sy).toNumber();
		}
		if (cansel_array[key].vid === '2')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].y = new Decimal(cansel_array[key].line_id[i][0].y).plus(sy).toNumber();
					cansel_array[key].line_id[i][1].y = new Decimal(cansel_array[key].line_id[i][1].y).plus(sy).toNumber();
				}
			}

			cansel_array[key].vert[1].y = new Decimal(cansel_array[key].vert[1].y).plus(sy).toNumber();
			cansel_array[key].vert[3].y = new Decimal(cansel_array[key].vert[3].y).plus(sy).toNumber();
		}
		if (cansel_array[key].vid === '3_1')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].y = new Decimal(cansel_array[key].line_id[i][0].y).plus(sy).toNumber();
					cansel_array[key].line_id[i][1].y = new Decimal(cansel_array[key].line_id[i][1].y).plus(sy).toNumber();
				}
			}
			for (var i in cansel_array[key].diag)
			{
				cansel_array[key].diag[i][0].y = new Decimal(cansel_array[key].diag[i][0].y).plus(sy).toNumber();
				cansel_array[key].diag[i][1].y = new Decimal(cansel_array[key].diag[i][1].y).plus(sy).toNumber();
			}

			cansel_array[key].vert[1].y = new Decimal(cansel_array[key].vert[1].y).plus(sy).toNumber();
			cansel_array[key].vert[3].y = new Decimal(cansel_array[key].vert[3].y).plus(sy).toNumber();
			cansel_array[key].vert[5].y = new Decimal(cansel_array[key].vert[5].y).plus(sy).toNumber();
		}
		if (cansel_array[key].vid === '3_mega')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].y = new Decimal(cansel_array[key].line_id[i][0].y).plus(sy).toNumber();
					cansel_array[key].line_id[i][1].y = new Decimal(cansel_array[key].line_id[i][1].y).plus(sy).toNumber();
				}
			}
			for (var i in cansel_array[key].diag)
			{
				cansel_array[key].diag[i][0].y = new Decimal(cansel_array[key].diag[i][0].y).plus(sy).toNumber();
				cansel_array[key].diag[i][1].y = new Decimal(cansel_array[key].diag[i][1].y).plus(sy).toNumber();
			}
		}
	}
}

function sdvig_sx(sx)
{
	for (var key in project.activeLayer.children)
	{
		project.activeLayer.children[key].position.x = new Decimal(project.activeLayer.children[key].position.x).plus(sx).toNumber();
	}

	for (var key in cansel_array)
	{
		if (cansel_array[key].vid === '1')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].x = new Decimal(cansel_array[key].line_id[i][0].x).plus(sx).toNumber();
					cansel_array[key].line_id[i][1].x = new Decimal(cansel_array[key].line_id[i][1].x).plus(sx).toNumber();
				}
			}
			cansel_array[key].start_point.x = new Decimal(cansel_array[key].start_point.x).plus(sx).toNumber();
			cansel_array[key].end_point.x = new Decimal(cansel_array[key].end_point.x).plus(sx).toNumber();
		}
		if (cansel_array[key].vid === '2')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].x = new Decimal(cansel_array[key].line_id[i][0].x).plus(sx).toNumber();
					cansel_array[key].line_id[i][1].x = new Decimal(cansel_array[key].line_id[i][1].x).plus(sx).toNumber();
				}
			}

			cansel_array[key].vert[1].x = new Decimal(cansel_array[key].vert[1].x).plus(sx).toNumber();
			cansel_array[key].vert[3].x = new Decimal(cansel_array[key].vert[3].x).plus(sx).toNumber();
		}
		if (cansel_array[key].vid === '3_1')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].x = new Decimal(cansel_array[key].line_id[i][0].x).plus(sx).toNumber();
					cansel_array[key].line_id[i][1].x = new Decimal(cansel_array[key].line_id[i][1].x).plus(sx).toNumber();
				}
			}
			for (var i in cansel_array[key].diag)
			{
				cansel_array[key].diag[i][0].x = new Decimal(cansel_array[key].diag[i][0].x).plus(sx).toNumber();
				cansel_array[key].diag[i][1].x = new Decimal(cansel_array[key].diag[i][1].x).plus(sx).toNumber();
			}

			cansel_array[key].vert[1].x = new Decimal(cansel_array[key].vert[1].x).plus(sx).toNumber();
			cansel_array[key].vert[3].x = new Decimal(cansel_array[key].vert[3].x).plus(sx).toNumber();
			cansel_array[key].vert[5].x = new Decimal(cansel_array[key].vert[5].x).plus(sx).toNumber();
		}
		if (cansel_array[key].vid === '3_mega')
		{
			for (var i in cansel_array[key].line_id)
			{
				if (cansel_array[key].line_id[i] !== null)
				{
					cansel_array[key].line_id[i][0].x = new Decimal(cansel_array[key].line_id[i][0].x).plus(sx).toNumber();
					cansel_array[key].line_id[i][1].x = new Decimal(cansel_array[key].line_id[i][1].x).plus(sx).toNumber();
				}
			}
			for (var i in cansel_array[key].diag)
			{
				cansel_array[key].diag[i][0].x = new Decimal(cansel_array[key].diag[i][0].x).plus(sx).toNumber();
				cansel_array[key].diag[i][1].x = new Decimal(cansel_array[key].diag[i][1].x).plus(sx).toNumber();
			}
		}
	}
}

function zoom(flag1)
{
	var l = 0;
	for (var key in lines)
	{
		l++;
	}
	if (l === 0)
	{
		return;
	}
	for (var key in lines)
	{
		lines[key].segments[0].point.x = parseFloat(lines[key].segments[0].point.x.toFixed(2));
		lines[key].segments[1].point.x = parseFloat(lines[key].segments[1].point.x.toFixed(2));
		lines[key].segments[0].point.y = parseFloat(lines[key].segments[0].point.y.toFixed(2));
		lines[key].segments[1].point.y = parseFloat(lines[key].segments[1].point.y.toFixed(2));
	}
	g_points = getPathsPoints(lines);
	var points_m = findMinAndMaxCordinate_g();
	add_granica();

	if (flag1 === 1)
	{
		if ((points_m.maxY >= down_g.position.y - 20
			&& points_m.minY <= up_g.position.y + 20)
			|| (points_m.maxX >= right_g.position.x - 20
			&& points_m.minX <= left_g.position.x + 20))
		{
			while ((points_m.maxY >= down_g.position.y
					|| points_m.minY <= up_g.position.y)
					|| (points_m.maxX >= right_g.position.x
					|| points_m.minX <= left_g.position.x))
			{
				if (view.zoom > 0.2)
				{
					view.zoom = new Decimal(view.zoom).minus(0.05).toNumber();
					clear_granica();
					sdvig();
					add_granica();
					if (view.zoom < 1)
					{
						for (var key = text_points.length; key--;)
						{
							text_points[key].fontSize += 2;
						}
						for (var key in text_lines)
						{
							text_lines[key].fontSize += 2;
						}
						for (var key = text_diag.length; key--;)
						{
							text_diag[key].fontSize += 2;
						}
						for (var key = text_contur.length; key--;)
						{
							text_contur[key].fontSize += 2;
						}
						if (view.zoom < 0.4)
						{
							for (var key = text_points.length; key--;)
							{
								text_points[key].fontSize += 4;
							}
							for (var key in text_lines)
							{
								text_lines[key].fontSize += 4;
							}
							for (var key = text_diag.length; key--;)
							{
								text_diag[key].fontSize += 4;
							}
							for (var key = text_contur.length; key--;)
							{
								text_contur[key].fontSize += 4;
							}
						}
					}
					points_m = findMinAndMaxCordinate_g();
				}
				else
				{
					clear_granica();
					return;
				}
			}
		}
	}
	if (flag1 === 2)
	{
		while ((points_m.maxY <= down_g.position.y
				|| points_m.minY >= up_g.position.y)
				|| (points_m.maxX <= right_g.position.x
				|| points_m.minX >= left_g.position.x))
		{
			if (view.zoom < 2)
			{
				view.zoom = new Decimal(view.zoom).plus(0.05).toNumber();
				clear_granica();
				sdvig();
				add_granica();
				if (view.zoom < 1)
				{
					for (var key = text_points.length; key--;)
					{
						text_points[key].fontSize -= 2;
					}
					for (var key in text_lines)
					{
						text_lines[key].fontSize -= 2;
					}
					for (var key = text_diag.length; key--;)
					{
						text_diag[key].fontSize -= 2;
					}
					for (var key = text_contur.length; key--;)
					{
						text_contur[key].fontSize -= 2;
					}
					if (view.zoom < 0.4)
					{
						for (var key = text_points.length; key--;)
						{
							text_points[key].fontSize -= 4;
						}
						for (var key in text_lines)
						{
							text_lines[key].fontSize -= 4;
						}
						for (var key = text_diag.length; key--;)
						{
							text_diag[key].fontSize -= 4;
						}
						for (var key = text_contur.length; key--;)
						{
							text_contur[key].fontSize -= 4;
						}
					}
				}
				points_m = findMinAndMaxCordinate_g();
			}
			else
			{
				clear_granica();
				return;
			}
		}
	}
	clear_granica();
}

function add_granica()
{
	var vw = view.viewSize.width,
		vh = view.viewSize.height,
		vwz = new Decimal(vw).dividedBy(view.zoom),
		vhz = new Decimal(vh).dividedBy(view.zoom);
	granica = new Path.Rectangle(0, 0, vwz.minus(80).toNumber(), vhz.minus(80).toNumber());
	granica.position = view.center;

	left_g = new Path.Line(granica.segments[0].point, granica.segments[1].point);
	up_g = new Path.Line(granica.segments[1].point, granica.segments[2].point);
	right_g = new Path.Line(granica.segments[2].point, granica.segments[3].point);
	down_g = new Path.Line(granica.segments[3].point, granica.segments[0].point);
}

function clear_granica()
{
	if (granica !== undefined && left_g !== undefined && up_g !== undefined && right_g !== undefined && down_g !== undefined)
	{
		granica.remove();
		left_g.remove();
		up_g.remove();
		right_g.remove();
		down_g.remove();
	}
}

function findMinAndMaxCordinate_g()
{
    if(g_points.length>0){
        var minX = g_points[0].x;
        var maxX = g_points[0].x;
        var minY = g_points[0].y;
        var maxY = g_points[0].y;
        for(var i=1;i<g_points.length;i++){
            if(g_points[i].x<minX){
                minX = g_points[i].x;
            }
            if(g_points[i].x>maxX){
                maxX = g_points[i].x;
            }
            if(g_points[i].y<minY){
                minY = g_points[i].y;
            }
            if(g_points[i].y>maxY){
                maxY = g_points[i].y;
            }
        }
        return {minX:minX,maxX:maxX,minY:minY,maxY:maxY};
    }
}

tool.onMouseDown = function(event)
{
	if (elem_useGrid.checked)
	{
		line_v = Path.Line();
		line_v.removeSegments();
		line_h = Path.Line();
		line_h.removeSegments();
		line_v.strokeColor = 'blue';
		line_v.strokeWidth = 3;
		line_h.strokeColor = 'blue';
		line_h.strokeWidth = 3;
		var l = 0;
		for (var key in lines)
		{
			l++;
		}
		if (l === 0)
		{
			move_point = event.point;
			start_point = event.point;
			begin_point = event.point;
			end_point = event.point;
			point_start_or_end = 'end';
			krug_end = Path.Circle();
			return;
		}
		else
		{
			if (start_point === undefined || end_point === undefined)
			{
				fix_point_dvig = event.point;
				return;
			}

			opred_rez = point_opredelenie(event.point);

			if (opred_rez !== null)
			{
				begin_point = opred_rez.point;
				point_start_or_end = opred_rez.vid;
				line_v.strokeColor = 'blue';
				line_v.strokeWidth = 3;
				line_h.strokeColor = 'blue';
				line_h.strokeWidth = 3;
				move_point = event.point;
			}
			else
			{
				begin_point = undefined;
				move_point = undefined;
				fix_point_dvig = event.point;
			}
		}
	}
	else
	{
		line_h = undefined;
		line_v = Path.Line();
		line_v.removeSegments();
		line_v.strokeColor = 'blue';
		line_v.strokeWidth = 3;
		var l = 0;
		for (var key in lines)
		{
			l++;
		}
		if (l === 0)
		{
			move_point = event.point;
			start_point = event.point;
			begin_point = event.point;
			end_point = event.point;
			point_start_or_end = 'end';
			krug_end = Path.Circle();
			return;
		}
		else
		{
			if (start_point === undefined || end_point === undefined)
			{
				fix_point_dvig = event.point;
				return;
			}

			opred_rez = point_opredelenie(event.point);
			if (opred_rez !== null)
			{
				begin_point = opred_rez.point;
				point_start_or_end = opred_rez.vid;
				line_v.strokeColor = 'blue';
				line_v.strokeWidth = 3;
				move_point = event.point;
			}
			else
			{
				begin_point = undefined;
				move_point = undefined;
				fix_point_dvig = event.point;
			}
		}
	}
};

function dvig_lines(napr)
{
	switch (napr)
	{
	  case '1':
	  	for (var key in project.activeLayer.children)
		{
			project.activeLayer.children[key].position.y -=  1;
		}

		for (var key in cansel_array)
		{
			if (cansel_array[key].vid === '1')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].y -= 1;
						cansel_array[key].line_id[i][1].y -= 1;
					}
				}
				cansel_array[key].start_point.y -= 1;
				cansel_array[key].end_point.y -= 1;
			}
		}

		move_point.y = view.viewSize.height / view.zoom - 20;
		begin_point.y -= 1;
		start_point.y -= 1;
		end_point.y -= 1;
	  	break;
	  case '2':
	  	for (var key in project.activeLayer.children)
		{
			project.activeLayer.children[key].position.y +=  1;
		}

		for (var key in cansel_array)
		{
			if (cansel_array[key].vid === '1')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].y += 1;
						cansel_array[key].line_id[i][1].y += 1;
					}
				}
				cansel_array[key].start_point.y += 1;
				cansel_array[key].end_point.y += 1;
			}
		}

		move_point.y = view.viewSize.height * (view.zoom - 1) + 20;
		start_point.y += 1;
		begin_point.y += 1;
		end_point.y += 1;
	  	break;
	  case '3':
	  	for (var key in project.activeLayer.children)
		{
			project.activeLayer.children[key].position.x -=  1;
		}

		for (var key in cansel_array)
		{
			if (cansel_array[key].vid === '1')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].x -= 1;
						cansel_array[key].line_id[i][1].x -= 1;
					}
				}
				cansel_array[key].start_point.x -= 1;
				cansel_array[key].end_point.x -= 1;
			}
		}

		move_point.x = view.viewSize.width / view.zoom - 20;
		start_point.x -= 1;
		begin_point.x -= 1;
		end_point.x -= 1;
	    break;
	  case '4':
	  	for (var key in project.activeLayer.children)
		{
			project.activeLayer.children[key].position.x +=  1;
		}

		for (var key in cansel_array)
		{
			if (cansel_array[key].vid === '1')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].x += 1;
						cansel_array[key].line_id[i][1].x += 1;
					}
				}
				cansel_array[key].start_point.x += 1;
				cansel_array[key].end_point.x += 1;
			}
		}

		move_point.x = view.viewSize.width * (view.zoom - 1) + 20;
		start_point.x += 1;
		begin_point.x += 1;
		end_point.x += 1;
	    break;
	}
	for (var key in lines)
	{
		lines[key].segments[0].point.x = parseFloat(lines[key].segments[0].point.x.toFixed(2));
		lines[key].segments[1].point.x = parseFloat(lines[key].segments[1].point.x.toFixed(2));
		lines[key].segments[0].point.y = parseFloat(lines[key].segments[0].point.y.toFixed(2));
		lines[key].segments[1].point.y = parseFloat(lines[key].segments[1].point.y.toFixed(2));
	}
	start_point.x = parseFloat(start_point.x.toFixed(2));
	start_point.y = parseFloat(start_point.y.toFixed(2));
	end_point.x = parseFloat(end_point.x.toFixed(2));
	end_point.y = parseFloat(end_point.y.toFixed(2));
	if (elem_useGrid.checked)
	{
		line_v.removeSegments();
		line_h.removeSegments();
		if (vh === 'v')
		{
			c_point = new Point(begin_point.x, move_point.y);
			line_v.addSegments([begin_point, c_point]);
			line_h.addSegments([c_point, move_point]);
		}
		else if (vh === 'h')
		{
			c_point = new Point(move_point.x, begin_point.y);
			line_h.addSegments([begin_point, c_point]);
			line_v.addSegments([c_point, move_point]);
		}
		if (line_v.length < 60 && line_h.length < 60)
		{
			if (line_v.length < line_h.length)
			{
				vh = 'h';
			}
			else if (line_v.length > line_h.length)
			{
				vh = 'v';
			}
		}
		line_v.strokeColor = 'blue';
		line_h.strokeColor = 'blue';
		line_bad = false;
		proverka();
		if (line_bad)
		{
			line_v.removeSegments();
			line_h.removeSegments();
			if (vh === 'h')
			{
				c_point = new Point(begin_point.x, move_point.y);
				line_v.addSegments([begin_point, c_point]);
				line_h.addSegments([c_point, move_point]);
			}
			else if (vh === 'v')
			{
				c_point = new Point(move_point.x, begin_point.y);
				line_h.addSegments([begin_point, c_point]);
				line_v.addSegments([c_point, move_point]);
			}
			line_bad = false;
			proverka();
			if (line_bad)
			{
				line_v.strokeColor = 'red';
				line_h.strokeColor = 'red';
			}
		}
	}
	else
	{
		line_v.removeSegments();
		line_v.addSegments([begin_point, move_point]);
		line_v.strokeColor = 'blue';
		line_bad = false;

		proverka();

		if (line_bad)
		{
			line_v.strokeColor = 'red';
		}
	}

}

tool.onMouseDrag = function(event)
{
	if (move_point === undefined)
	{
		var rast_x = Math.round(fix_point_dvig.x - event.point.x);
		var rast_y = Math.round(fix_point_dvig.y - event.point.y);
		for (var key in project.activeLayer.children)
		{
			project.activeLayer.children[key].position.x -=  rast_x;
			project.activeLayer.children[key].position.y -=  rast_y;
		}
		for (var key in cansel_array)
		{
			if (cansel_array[key].vid === '1')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].x -= rast_x;
						cansel_array[key].line_id[i][1].x -= rast_x;
						cansel_array[key].line_id[i][0].y -= rast_y;
						cansel_array[key].line_id[i][1].y -= rast_y;
					}
				}
				cansel_array[key].start_point.x -= rast_x;
				cansel_array[key].end_point.x -= rast_x;
				cansel_array[key].start_point.y -= rast_y;
				cansel_array[key].end_point.y -= rast_y;
			}
			if (cansel_array[key].vid === '2')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].x -= rast_x;
						cansel_array[key].line_id[i][1].x -= rast_x;
						cansel_array[key].line_id[i][0].y -= rast_y;
						cansel_array[key].line_id[i][1].y -= rast_y;
					}
				}
				//cansel_array[key].vert[0].x -= rast_x;
				cansel_array[key].vert[1].x -= rast_x;
				//cansel_array[key].vert[2].x -= rast_x;
				cansel_array[key].vert[3].x -= rast_x;
				//cansel_array[key].vert[0].y -= rast_y;
				cansel_array[key].vert[1].y -= rast_y;
				//cansel_array[key].vert[2].y -= rast_y;
				cansel_array[key].vert[3].y -= rast_y;
			}
			if (cansel_array[key].vid === '3_1')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].x -= rast_x;
						cansel_array[key].line_id[i][1].x -= rast_x;
						cansel_array[key].line_id[i][0].y -= rast_y;
						cansel_array[key].line_id[i][1].y -= rast_y;
					}
				}
				for (var i in cansel_array[key].diag)
				{
					cansel_array[key].diag[i][0].x -= rast_x;
					cansel_array[key].diag[i][1].x -= rast_x;
					cansel_array[key].diag[i][0].y -= rast_y;
					cansel_array[key].diag[i][1].y -= rast_y;
				}
				//cansel_array[key].vert[0].x -= rast_x;
				cansel_array[key].vert[1].x -= rast_x;
				//cansel_array[key].vert[2].x -= rast_x;
				cansel_array[key].vert[3].x -= rast_x;
				//cansel_array[key].vert[4].x -= rast_x;
				cansel_array[key].vert[5].x -= rast_x;
				//cansel_array[key].vert[0].y -= rast_y;
				cansel_array[key].vert[1].y -= rast_y;
				//cansel_array[key].vert[2].y -= rast_y;
				cansel_array[key].vert[3].y -= rast_y;
				//cansel_array[key].vert[4].y -= rast_y;
				cansel_array[key].vert[5].y -= rast_y;
			}
			if (cansel_array[key].vid === '3_mega')
			{
				for (var i in cansel_array[key].line_id)
				{
					if (cansel_array[key].line_id[i] !== null)
					{
						cansel_array[key].line_id[i][0].x -= rast_x;
						cansel_array[key].line_id[i][1].x -= rast_x;
						cansel_array[key].line_id[i][0].y -= rast_y;
						cansel_array[key].line_id[i][1].y -= rast_y;
					}
				}
				for (var i in cansel_array[key].diag)
				{
					cansel_array[key].diag[i][0].x -= rast_x;
					cansel_array[key].diag[i][1].x -= rast_x;
					cansel_array[key].diag[i][0].y -= rast_y;
					cansel_array[key].diag[i][1].y -= rast_y;
				}
			}
		}
		if (start_point !== undefined || end_point !== undefined)
		{
			start_point.x -= rast_x;
			end_point.x -= rast_x;
			start_point.y -= rast_y;
			end_point.y -= rast_y;		
		}
		fix_point_dvig = event.point;
		return;
	}
	if (elem_useGrid.checked)
	{
		line_v.removeSegments();
		line_h.removeSegments();

		opred_rez = point_opredelenie(event.point);
		if (opred_rez !== null)
		{
			move_point = opred_rez.point;
		}
		else
		{
			move_point = event.point;
		}

		add_granica();

		if (move_point.y > down_g.position.y)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '1');
		}
		else if (move_point.y < up_g.position.y)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '2');
		}
		else if (move_point.x > right_g.position.x)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '3');
		}
		else if (move_point.x < left_g.position.x)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '4');
		}
		else
		{
			clearInterval(timer1);
		}

		clear_granica();

		if (vh === 'v')
		{
			c_point = new Point(begin_point.x, move_point.y);
			line_v.addSegments([begin_point, c_point]);
			line_h.addSegments([c_point, move_point]);
		}
		else if (vh === 'h')
		{
			c_point = new Point(move_point.x, begin_point.y);
			line_h.addSegments([begin_point, c_point]);
			line_v.addSegments([c_point, move_point]);
		}
		
		if (line_v.length < 60 && line_h.length < 60)
		{
			if (line_v.length < line_h.length)
			{
				vh = 'h';
			}
			else if (line_v.length > line_h.length)
			{
				vh = 'v';
			}
		}
		line_v.strokeColor = 'blue';
		line_h.strokeColor = 'blue';

		line_bad = false;

		proverka();

		if (line_bad)
		{
			line_v.removeSegments();
			line_h.removeSegments();

			if (vh === 'h')
			{
				c_point = new Point(begin_point.x, move_point.y);
				line_v.addSegments([begin_point, c_point]);
				line_h.addSegments([c_point, move_point]);
			}
			else if (vh === 'v')
			{
				c_point = new Point(move_point.x, begin_point.y);
				line_h.addSegments([begin_point, c_point]);
				line_v.addSegments([c_point, move_point]);
			}
			
			line_bad = false;
			proverka();
			if (line_bad)
			{
				line_v.strokeColor = 'red';
				line_h.strokeColor = 'red';

			}
		}
	}
	else
	{
		line_v.removeSegments();

		opred_rez = point_opredelenie(event.point);
		if (opred_rez !== null)
		{
			move_point = opred_rez.point;
		}
		else
		{
			move_point = event.point;
		}

		if (move_point.y - view.viewSize.height * (view.zoom - 1) > view.viewSize.height / view.zoom - 40)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '1');
		}
		else if (move_point.y < view.viewSize.height * (view.zoom - 1) + 40)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '2');
		}
		else if (move_point.x - view.viewSize.width * (view.zoom - 1) > view.viewSize.width / view.zoom - 40)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '3');
		}
		else if (move_point.x < view.viewSize.width * (view.zoom - 1) + 40)
		{
			clearInterval(timer1);
			timer1 = setInterval(dvig_lines, 1, '4');
		}
		else
		{
			clearInterval(timer1);
		}

		line_v.addSegments([begin_point, move_point]);
		line_v.strokeColor = 'blue';

		line_bad = false;

		proverka();

		if (line_bad)
		{
			line_v.strokeColor = 'red';
		}
	}
	if (!line_bad)
	{
		if (point_start_or_end === 'start' && point_ravny(move_point, end_point)
			|| point_start_or_end === 'end' && point_ravny(move_point, start_point))
		{
			for (var key in lines)
			{
				lines[key].strokeColor = 'darkorange';
			}
			line_v.strokeColor = 'darkorange';
			if (elem_useGrid.checked)
			{
				line_h.strokeColor = 'darkorange';
			}
		}
		else
		{
			for (var key in lines)
			{
				lines[key].strokeColor = 'black';
			}
			line_v.strokeColor = 'blue';
			if (elem_useGrid.checked)
			{
				line_h.strokeColor = 'blue';
			}
		}
	}
	else
	{
		for (var key in lines)
		{
			lines[key].strokeColor = 'black';
		}
		line_v.strokeColor = 'red';
		if (elem_useGrid.checked)
		{
			line_h.strokeColor = 'red';
		}
	}
};

function proverka()
{
	var point_near_lines_rez, point_near_lines_rez0, point_near_lines_rez1, line_on_lines_rez_v, line_on_lines_rez_h, line_point0, line_point1;
	if (elem_useGrid.checked)
	{
		point_near_lines_rez = point_near_lines(move_point);
		if (point_near_lines_rez.length >= 2 && opred_rez === null)
		{
			for (var key in point_near_lines_rez)
			{
				if (!lines_ravny(point_near_lines_rez[key], line_v) && !lines_ravny(point_near_lines_rez[key], line_h))
				{
					line_bad = true;
					break;
				}
			}	
		}

		point_near_lines_rez = point_near_lines(c_point);
		if (point_near_lines_rez.length >= 3 && opred_rez === null)
		{
			for (var key in point_near_lines_rez)
			{
				if (!lines_ravny(point_near_lines_rez[key], line_v) && !lines_ravny(point_near_lines_rez[key], line_h))
				{
					line_bad = true;
					break;
				}
			}	
		}
	}
	else
	{
		point_near_lines_rez = point_near_lines(move_point);
		if (point_near_lines_rez.length >= 2 && opred_rez === null)
		{
			for (var key in point_near_lines_rez)
			{
				if (!lines_ravny(point_near_lines_rez[key], line_v))
				{
					line_bad = true;
					break;
				}
			}	
		}
	}

	if (elem_useGrid.checked)
	{
		line_on_lines_rez_v = line_on_lines(line_v);
		line_on_lines_rez_h = line_on_lines(line_h);
		if ((line_on_lines_rez_v.length + line_on_lines_rez_h.length >= 2 && opred_rez === null) 
			|| (line_on_lines_rez_v.length + line_on_lines_rez_h.length >= 3 && opred_rez !== null))
		{
			line_bad = true;
		}
	}
	else
	{
		line_on_lines_rez_v = line_on_lines(line_v);
		if ((line_on_lines_rez_v.length >= 2 && opred_rez === null) 
			|| (line_on_lines_rez_v.length >= 3 && opred_rez !== null))
		{
			line_bad = true;
		}
	}

	for (var key in lines)
	{
		line_point0 = lines[key].segments[0].point;
		line_point1 = lines[key].segments[1].point;
		point_near_lines_rez0 = point_near_lines(line_point0);
		point_near_lines_rez1 = point_near_lines(line_point1);
		if ((point_near_lines_rez0.length > 2 || point_near_lines_rez1.length > 2) && opred_rez === null)
		{
			line_bad = true;
			break;
		}
	}

	if (point_near_lines(start_point).length > 1 && opred_rez === null && !point_ravny(begin_point, start_point))
	{
		line_bad = true;
	}

	if (point_near_lines(end_point).length > 1 && opred_rez === null && !point_ravny(begin_point, end_point))
	{
		line_bad = true;
	}
}

tool.onMouseUp = function(event)
{
	touch1 = undefined;
	touch2 = undefined;
	fix_point_dvig = undefined;
	clearInterval(timer1);
	if (move_point === undefined)
	{
		return;
	}
	var good_lines, line_pr;
	if (elem_useGrid.checked)
	{
		if (line_bad)
		{
			line_v.remove();
			line_h.remove();
			return;
		}
		good_lines = false;
		count_cansel++;
		cansel_array[count_cansel] = {line_id: [], start_point: start_point.clone(), end_point: end_point.clone(), vid: '1'};
		var l = 0;
		for (var key in lines)
		{
			l++;
		}
		if (l === 0)
		{
			krug_start = new Path.Circle(begin_point, 40);
			krug_start.strokeColor = 'green';
		}

		if (point_ravny(begin_point, start_point) && point_ravny(move_point, end_point) 
			|| point_ravny(move_point, start_point) && point_ravny(begin_point, end_point) 
			|| line_v.length >= 40 && line_h.length >= 40)
		{
			if (line_v.length > 3 || line_prodolzhenie(line_v)[0] !== null)
			{
				line_v.strokeColor = 'black';
				line_pr = line_prodolzhenie(line_v);
				if (line_pr.length === 2)
				{
					line_v.remove();
					prodolzhit_line(line_pr[0], line_pr[1]);
				}
				else
				{
					prodolzhit_line(line_pr[0], line_v);
				}
			}
			else
			{
				line_v.remove();
				if (line_prodolzhenie(line_h)[0] === null)
				{
					line_h.removeSegments();
					line_h.addSegments([begin_point, move_point]);
				}
				else if (line_prodolzhenie(line_h).length !== 2)
				{
					line_h.removeSegments();
					line_h.addSegments([begin_point, move_point]);
				}
			}

			if (line_h.length > 3 || line_prodolzhenie(line_h)[0] !== null)
			{
				line_h.strokeColor = 'black';
				line_pr = line_prodolzhenie(line_h);
				if (line_pr.length === 2)
				{
					line_h.remove();
					prodolzhit_line(line_pr[0], line_pr[1]);
				}
				else
				{
					prodolzhit_line(line_pr[0], line_h);
				}
			}
			else
			{
				line_h.remove();
				if (line_v !== undefined)
				{
					if (line_prodolzhenie(line_v)[0] === null)
					{
						line_v.removeSegments();
						line_v.addSegments([begin_point, move_point]);
					}
					else if (line_prodolzhenie(line_v).length !== 2)
					{
						line_v.removeSegments();
						line_v.addSegments([begin_point, move_point]);
					}
				}
			}
			good_lines = true;
		}

		if (line_v.length < 40 && line_h.length < 40 && !good_lines)
		{
			line_v.removeSegments();
			line_v.remove();
			line_h.removeSegments();
			line_h.remove();
			count_cansel--;
			var l = 0;
			for (var key in lines)
			{
				l++;
			}
			if (l === 0)
			{
				krug_start.remove();
			}
			return;
		}
		
		if (line_v.length >= 40 && !good_lines)
		{
			line_h.removeSegments();
			line_h.remove();
			line_v.strokeColor = 'black';
			move_point = c_point;
			line_pr = line_prodolzhenie(line_v);
			prodolzhit_line(line_pr[0], line_v);
		}
		if (line_h.length >= 40 && !good_lines)
		{
			line_v.removeSegments();
			line_v.remove();
			line_h.strokeColor = 'black';
			var l = 0;
			for (var key in lines)
			{
				l++;
			}
			if (l === 0 && vh === 'v')
			{
				krug_start.remove();
				begin_point = c_point;
				start_point = c_point;
				krug_start = new Path.Circle(begin_point, 40);
				krug_start.strokeColor = 'green';
			}
			else
			{
				move_point = c_point;
			}
			line_pr = line_prodolzhenie(line_h);
			prodolzhit_line(line_pr[0], line_h);
		}
	}
	else
	{
		elem_useGrid.checked = true;
		if (line_bad)
		{
			line_v.remove();
			return;
		}
		good_lines = false;
		count_cansel++;
		cansel_array[count_cansel] = {line_id: [], start_point: start_point.clone(), end_point: end_point.clone(), vid: '1'};
		var l = 0;
		for (var key in lines)
		{
			l++;
		}
		if (l === 0)
		{
			krug_start = new Path.Circle(begin_point, 40);
			krug_start.strokeColor = 'green';
		}
		if (point_ravny(begin_point, start_point) && point_ravny(move_point, end_point) 
			|| point_ravny(move_point, start_point) && point_ravny(begin_point, end_point) 
			|| line_v.length >= 40)
		{
			if (line_v.length < 2)
			{
				line_v.removeSegments();
				line_v.remove();
				//text_v.remove();
				count_cansel--;
				l = 0;
				for (var key in lines)
				{
					l++;
				}
				if (l === 0)
				{
					krug_start.remove();
				}
				return;
			}
			line_v.strokeColor = 'black';
			line_pr = line_prodolzhenie(line_v);
			prodolzhit_line(line_pr[0], line_v);
		}
		else
		{
			line_v.removeSegments();
			line_v.remove();
			//text_v.remove();
			count_cansel--;
			l = 0;
			for (var key in lines)
			{
				l++;
			}
			if (l === 0)
			{
				krug_start.remove();
			}
			return;
		}
	}

	var circle = new Path.Circle(move_point, 40);
	circle.strokeColor = 'green';
	if (point_start_or_end === 'start')
	{
		start_point = move_point;
		krug_start.remove();
		krug_start = circle;
	}
	else if (point_start_or_end === 'end')
	{
		end_point = move_point;
		krug_end.remove();
		krug_end = circle;
	}

	point_start_or_end = undefined;
	if (point_ravny(start_point, end_point))
	{
		krug_end.remove();
		krug_start.remove();
		var l = 0;
		for (var key in lines)
		{
			l++;
		}
		if (l < 3)
		{
			count_cansel--;
			begin_point = undefined;
			move_point = undefined;
			c_point = undefined;
			return;
		}
		for (var key in lines)
		{
			lines[key].strokeColor = 'black';
		}

		chert_close = true;
		start_point = undefined;
		end_point = undefined;
		draw_lines_text();
		points = getPathsPoints(lines);
		text_points = drawLabels();
		sort_sten();
		resize_wall_begin();
	}
	begin_point = undefined;
	move_point = undefined;
	c_point = undefined;
};

function text_points_sdvig()
{
	var angle_line = 0;
	for (var key in lines)
	{
		angle_line = get_angle(lines[key].segments[0].point, lines[key].segments[1].point);
		if (angle_line > 60 && angle_line < 120 || angle_line > 240 && angle_line < 300)
		{
			if (lines[key].segments[0].point.x < lines[key].segments[1].point.x)
			{
				for (var i = text_points.length; i--;)
				{
					if (point_ravny(new Point(lines[key].segments[1].point.x - 10, lines[key].segments[1].point.y - 5), text_points[i].point))
					{
						text_points[i].point.x += 20;
					}
				}
			}
			if (lines[key].segments[0].point.x > lines[key].segments[1].point.x)
			{
				for (var i = text_points.length; i--;)
				{
					if (point_ravny(new Point(lines[key].segments[0].point.x - 10, lines[key].segments[0].point.y - 5), text_points[i].point))
					{
						text_points[i].point.x += 20;
					}
				}
			}
		}
	}
	for (var key = text_points.length; key--;)
	{
		text_points[key].bringToFront();
	}
}

function prodolzhit_line(line_pr, line)
{
	var p1, p2;
	if (line_pr !== null)
	{
		points = [line_pr.segments[0].point.clone(), line_pr.segments[1].point.clone()];
		cansel_array[count_cansel].line_id[line_pr.id] = points;
		var max_rast = 0;
		for (var i = line.segments.length; i--;)
		{
			for (var j = line_pr.segments.length; j--;)
			{
				if (max_rast < Math.sqrt(Math.pow(line.segments[i].point.x - line_pr.segments[j].point.x, 2) 
							+ Math.pow(line.segments[i].point.y - line_pr.segments[j].point.y, 2)))
				{
					max_rast = Math.sqrt(Math.pow(line.segments[i].point.x - line_pr.segments[j].point.x, 2) 
							+ Math.pow(line.segments[i].point.y - line_pr.segments[j].point.y, 2));
					p1 = line_pr.segments[j].point;
					p2 = line.segments[i].point;
				}
			}
		}

		
		if (lines[line.id] !== undefined)
		{
			points = [line.segments[0].point.clone(), line.segments[1].point.clone()];
			cansel_array[count_cansel].line_id[line.id] = points;
			delete lines[line.id];
		}
		line.remove();
		line_pr.removeSegments();
		line_pr.addSegments([p1, p2]);
	}
	else
	{
		cansel_array[count_cansel].line_id[line.id] = null;
		lines[line.id] = line;
	}
}

function draw_lines_text()
{
	for (var key in lines)
	{
		add_text_v_or_h(lines[key].id);
	}
}

function add_text_v_or_h(line_id)
{
	if (text_lines[line_id] !== undefined)
	{
		text_lines[line_id].remove();
	}

	text_lines[line_id] = new PointText();

	text_lines[line_id].fontWeight = 'bold';
	text_lines[line_id].fontSize = 16;
	var f = 1;
	while (f > view.zoom)
	{
		f -= 0.05;
		if (f < 1)
		{
			text_lines[line_id].fontSize += 2;
			if (f < 0.4)
			{
				text_lines[line_id].fontSize += 4;
			}
		}
	}
	text_lines[line_id].content = Math.round(lines[line_id].length);
	var angle = (Math.atan((lines[line_id].segments[1].point.y-lines[line_id].segments[0].point.y)/(lines[line_id].segments[1].point.x
		-lines[line_id].segments[0].point.x))*180)/Math.PI;
    text_lines[line_id].rotate(angle);
    text_lines[line_id].position = lines[line_id].position;
}

function add_text_diag(index)
{
	if (text_diag[index] !== undefined)
	{
		text_diag[index].remove();
	}
	text_diag[index] = new PointText();
	text_diag[index].fontWeight = 'bold';
	text_diag[index].fontSize = 16;
	var f = 1;
	while (f > view.zoom)
	{
		f -= 0.05;
		if (f < 1)
		{
			text_diag[index].fontSize += 2;
			if (f < 0.4)
			{
				text_diag[index].fontSize += 4;
			}
		}
	}
	text_diag[index].content = Math.round(diag_sort[index].length);
	var angle = (Math.atan((diag_sort[index].segments[1].point.y-diag_sort[index].segments[0].point.y)/(diag_sort[index].segments[1].point.x
			-diag_sort[index].segments[0].point.x))*180)/Math.PI;
    text_diag[index].rotate(angle);
    text_diag[index].position = new Point(diag_sort[index].position.x, diag_sort[index].position.y);
}

function line_prodolzhenie(line)
{
	var mass = [];
	if (line.segments.length < 2)
	{
		mass[0] = null;
		return mass;
	}
	var hitResults0 = project.hitTestAll(line.segments[0].point, {class: Path, segments: true, tolerance: 3});
	var hitResults1 = project.hitTestAll(line.segments[1].point, {class: Path, segments: true, tolerance: 3});
	for (var key = hitResults0.length; key--;)
	{
		if (line === hitResults0[key].item)
		{
			continue;
		}
		if (line_h_or_v(line) === line_h_or_v(hitResults0[key].item))
		{
			if (line_h_or_v(line) === null && line_h_or_v(hitResults0[key].item) === null)
			{
				break;
			}
			if (!in_array(mass, hitResults0[key].item))
			{
				mass.push(hitResults0[key].item);
			}
		}
	}
	for (var key = hitResults1.length; key--;)
	{
		if (line === hitResults1[key].item)
		{
			continue;
		}
		if (line_h_or_v(line) === line_h_or_v(hitResults1[key].item))
		{
			if (line_h_or_v(line) === null && line_h_or_v(hitResults1[key].item) === null)
			{
				break;
			}
			if (!in_array(mass, hitResults1[key].item))
			{
				mass.push(hitResults1[key].item);
			}
		}
	}
	if (mass.length === 0)
	{
		mass[0] = null;
	}
	return mass;
}

function line_h_or_v(line)
{
	if (line.firstSegment.point.x === line.lastSegment.point.x)
	{
		return 'v';
	}
	else if (line.firstSegment.point.y === line.lastSegment.point.y)
	{
		return 'h';
	}
	else
	{
		return null;
	}
}
function point_opredelenie(point)
{
	var ret_rez = null;
	var hitResults = project.hitTestAll(point, {class: Path, segments: true, tolerance: 40});
	for (var key in hitResults)
	{
		if (hitResults[key].item.segments.length === 2)
		{
			if (point_ravny(hitResults[key].point, start_point))
			{
				ret_rez = {point: hitResults[key].point, vid: 'start'};
			}
			else if (point_ravny(hitResults[key].point, end_point))
			{
				ret_rez = {point: hitResults[key].point, vid: 'end'};
			}
		}
	}
	return ret_rez;
}

function line_on_lines(item)
{
	var ret_rez = [];
	
	for (var key in lines)
	{
		if (lines[key].intersects(item))
		{
			ret_rez.push(lines[key]);	
		}
	}
	return ret_rez;
}

function point_ravny(point1, point2)
{
	if (point1.x > point2.x - 1 && point1.x < point2.x + 1 && point1.y > point2.y - 1 && point1.y < point2.y + 1)
	{
		return true;
	}
	else
	{
		return false;
	}
}
function lines_ravny(line1, line2)
{
	if (line1.firstSegment.point.x === line2.firstSegment.point.x 
		&& line1.firstSegment.point.y === line2.firstSegment.point.y
		&& line1.lastSegment.point.x === line2.lastSegment.point.x
		&& line1.lastSegment.point.y === line2.lastSegment.point.y 
		|| line1.firstSegment.point.x === line2.lastSegment.point.x 
		&& line1.firstSegment.point.y === line2.lastSegment.point.y
		&& line1.lastSegment.point.x === line2.firstSegment.point.x
		&& line1.lastSegment.point.y === line2.firstSegment.point.y)
	{
		return true;
	}
	else
	{
		return false;
	}
}

function point_near_lines(point)
{
	var ret_rez = [];
	var hitResults = project.hitTestAll(point, {class: Path, segments: true, stroke: true, tolerance: 38});
	for (var key in hitResults)
	{
		if (hitResults[key].item.segments.length === 2)
		{
			ret_rez.push(hitResults[key].item);
		}
	}
	return ret_rez;
}

function cancelLastAction()
{
	ready = false;
	if (count_cansel > 0)
	{
		if (count_cansel === 1)
		{
			clear_elem();
		}
		else
		{
			clearInterval(timer_mig);
			if (cansel_array[count_cansel].vid === '1')
			{
				elem_window.style.display = 'none';
				for (var key in text_lines)
				{
					text_lines[key].remove();
				}
	    		for (var key in cansel_array[count_cansel].line_id)
	    		{
	    			if (cansel_array[count_cansel].line_id[key] === null)
	    			{
	    				lines[key].remove();
	    				delete text_lines[key];
	    				delete lines[key];
	    			}
	    			else
	    			{
	    				if (lines[key] === undefined)
	    				{
	    					var line = Path.Line(cansel_array[count_cansel].line_id[key][0], cansel_array[count_cansel].line_id[key][1]);
	    					line.strokeColor = 'black';
	    					line.strokeWidth = 3;
	    					lines[line.id] = line;
	    					for (var i in cansel_array)
	    					{
	    						if (cansel_array[i].line_id[key] !== undefined)
	    						{
	    							cansel_array[i].line_id[line.id] = cansel_array[i].line_id[key];
	    							delete cansel_array[i].line_id[key];
	    						}
	    					}
	    				}
	    				else
	    				{
		    				lines[key].removeSegments();
		    				lines[key].addSegments([cansel_array[count_cansel].line_id[key][0], cansel_array[count_cansel].line_id[key][1]]);
		    			}
	    			}
	    		}
	    		start_point = cansel_array[count_cansel].start_point;
	    		end_point = cansel_array[count_cansel].end_point;
	    		krug_start.remove();
	    		krug_start = new Path.Circle(start_point, 40);
				krug_start.strokeColor = 'green';
				krug_end.remove();
	    		krug_end = new Path.Circle(end_point, 40);
				krug_end.strokeColor = 'green';
				for (var key = text_points.length; key--;)
		    	{
		    		text_points[key].remove();
		    	}
		    	for (var key in lines)
			    {
			    	lines[key].strokeColor = 'black';
			    }
		    	text_points = [];
		    	lines_sort = [];
		    	fixed_walls = [];
		    	chert_close = false;
		    	code = 64;
				alfavit = 0;
				delete cansel_array[count_cansel];
				count_cansel--;
			}
			if (cansel_array[count_cansel].vid === '2')
			{
				triangulate_bool = false;
				elem_jform_n5.value = '';
				elem_jform_n9.value = '';
				for (var key in cansel_array[count_cansel].line_id)
	    		{
    				lines[key].removeSegments();
    				lines[key].addSegments([cansel_array[count_cansel].line_id[key][0], cansel_array[count_cansel].line_id[key][1]]);
    				add_text_v_or_h(key);
	    		}
	    		text_points[cansel_array[count_cansel].vert[0]].point = cansel_array[count_cansel].vert[1];
    			text_points[cansel_array[count_cansel].vert[2]].point = cansel_array[count_cansel].vert[3];
	    		fixed_walls[cansel_array[count_cansel].fixed_id] = false;
	    		for (var key in cansel_array[count_cansel].razv_walls)
	    		{
	    			razv_wall[cansel_array[count_cansel].razv_walls[key]] = false;
	    		}
				for (var key = 0; key < lines_sort.length; key++)
			    {
			    	lines_sort[key].strokeColor = 'black';
			    }

				for (var key = 0; key < lines_sort.length; key++)
			    {
			    	if (fixed_walls[lines_sort[key].id])
			    	{
			    		lines_sort[key].strokeColor = 'green';
			    	}
			    	else
			    	{
			    		elem_window.style.display = 'block';
			    		lines_sort[key].strokeColor = 'red';
			    		text_lines[lines_sort[key].id].fillColor = 'Maroon';
			    		timer_mig = setInterval(migalka, 500, lines_sort[key]);
			    		elem_newLength.focus();
			    		elem_newLength.value = Math.round(lines_sort[key].length);
			    		elem_newLength.select();
			    		first_click = false;
			    		break;
			    	}
			    }
			    for (var key = text_diag.length; key--;)
		    	{
		    		text_diag[key].remove();
		    	}
		    	text_diag = [];
		    	for (var key = diag_sort.length; key--;)
		    	{
		    		diag_sort[key].remove();
		    	}
		    	diag_sort = [];
		    	diag = [];
		    	
			    delete cansel_array[count_cansel];
				count_cansel--;
			}
			if (cansel_array[count_cansel].vid === '3_1')
			{
				elem_jform_n4.value = '';
				for (var key in cansel_array[count_cansel].line_id)
	    		{
    				lines[key].removeSegments();
    				lines[key].addSegments([cansel_array[count_cansel].line_id[key][0], cansel_array[count_cansel].line_id[key][1]]);
    				add_text_v_or_h(key);
	    		}
	    		for (var key in cansel_array[count_cansel].diag)
	    		{
    				diag_sort[key].removeSegments();
    				diag_sort[key].addSegments([cansel_array[count_cansel].diag[key][0], cansel_array[count_cansel].diag[key][1]]);
    				add_text_diag(key);
	    		}
	    		text_points[cansel_array[count_cansel].vert[0]].point = cansel_array[count_cansel].vert[1];
    			text_points[cansel_array[count_cansel].vert[2]].point = cansel_array[count_cansel].vert[3];
    			text_points[cansel_array[count_cansel].vert[4]].point = cansel_array[count_cansel].vert[5];
				
				fixed_diags[cansel_array[count_cansel].fixed_id] = false;
				elem_window.style.display = 'block';

				for (var key in diag)
			    {
			    	diag[key].strokeColor = 'black';
			    }

				for (var key = 0; key < diag_sort.length; key++)
			    {
			    	if (fixed_diags[diag_sort[key].id])
			    	{
			    		diag_sort[key].strokeColor = 'green';
			    	}
			    	else
			    	{
			    		diag_sort[key].strokeColor = 'red';
			    		text_diag[key].fillColor = 'Maroon';
			    		timer_mig = setInterval(migalka, 500, diag_sort[key]);
			    		elem_newLength.focus();
			    		elem_newLength.value = Math.round(diag_sort[key].length);
			    		elem_newLength.select();
			    		first_click = false;
			    		break;
			    	}
			    }

			    for (var key in triangles)
		    	{
		    		triangles[key].remove();
		    	}
		    	triangles = [];

				delete cansel_array[count_cansel];
				count_cansel--;
			}
			if (cansel_array[count_cansel].vid === '3_mega')
			{
				elem_jform_n4.value = '';
				for (var key in cansel_array[count_cansel].line_id)
	    		{
    				lines[key].removeSegments();
    				lines[key].addSegments([cansel_array[count_cansel].line_id[key][0], cansel_array[count_cansel].line_id[key][1]]);
    				add_text_v_or_h(key);
	    		}
	    		for (var key in cansel_array[count_cansel].diag)
	    		{
    				diag_sort[key].removeSegments();
    				diag_sort[key].addSegments([cansel_array[count_cansel].diag[key][0], cansel_array[count_cansel].diag[key][1]]);
    				add_text_diag(key);
	    		}

	    		var j = 0;
	    		for (var i = 0; i < lines_sort.length; i++)
			    {
			    	if (i === 0)
			    	{
			    		j = lines_sort.length - 1;
			    	}
			    	else
			    	{
			    		j = i - 1;
			    	}
			    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
			    	{
			    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
			    	}
			    }
				
				fixed_diags[cansel_array[count_cansel].fixed_id] = false;

				elem_window.style.display = 'block';


				for (var key = 0; key < diag_sort.length; key++)
			    {
			    	if (fixed_diags[diag_sort[key].id])
			    	{
			    		diag_sort[key].strokeColor = 'green';
			    	}
			    	else
			    	{
			    		diag_sort[key].strokeColor = 'black';
			    	}

			    	if (diag_sort[key].id === cansel_array[count_cansel].fixed_id)
			    	{
			    		diag_sort[key].strokeColor = 'red';
			    		text_diag[key].fillColor = 'Maroon';
			    		timer_mig = setInterval(migalka, 500, diag_sort[key]);
			    		elem_newLength.focus();
			    		elem_newLength.value = Math.round(diag_sort[key].length);
			    		elem_newLength.select();
			    		first_click = false;
			    	}
			    }

			    for (var key in triangles)
		    	{
		    		triangles[key].remove();
		    	}
		    	triangles = [];

				delete cansel_array[count_cansel];
				count_cansel--;
			}

			sdvig();
			zoom(1);
		}
	}
}

function clear_elem()
{
	for (var key in project.activeLayer.children)
	{
		project.activeLayer.children[key].remove();
	}
	view.zoom = 1;
	elem_window.style.display = 'none';
	cansel_array = [];
	count_cansel = 0;
	if (krug_start !== undefined)
	{
		krug_start.remove();
	}
	if (krug_end !== undefined)
	{
		krug_end.remove();
	}
	for (var key in lines)
	{
		lines[key].remove();
	}
	for (var key in text_lines)
	{
		text_lines[key].remove();
	}
	code = 64;
	alfavit = 0;
	lines = [];
	lines_sort = [];
	fixed_walls = [];
	razv_wall = [];
	g_points = [];
	triangulate_bool = false;
	vh = 'v';
	chert_close = false;
	start_point = undefined;
	end_point = undefined;
	for (var key = text_points.length; key--;)
	{
		text_points[key].remove();
	}
	text_points = [];
	for (var key = text_diag.length; key--;)
	{
		text_diag[key].remove();
	}
	text_diag = [];
	for (var key in diag)
	{
		diag[key].remove();
	}
	diag = [];
	diag_sort = [];
	fixed_diags = [];
	razv_wall_kos = [];
	coef_wall_kos = [];
	for (var key in triangles)
	{
		triangles[key].remove();
	}
	triangles = [];
	elem_jform_n4.value = '';
	elem_jform_n5.value = '';
	elem_jform_n9.value = '';
	ready = false;
	close_sketch_click_bool = false;
	resize_canvas();
}

function resize_wall_begin()
{
    for (var key = 0; key < lines_sort.length; key++)
    {
    	if (!fixed_walls[lines_sort[key].id])
    	{
    		elem_window.style.display = 'block';
    		elem_newLength.focus();
    		elem_newLength.value = Math.round(lines_sort[key].length);
    		elem_newLength.select();
    		first_click = false;
    		lines_sort[key].strokeColor = 'red';
    		text_lines[lines_sort[key].id].fillColor = 'Maroon';
    		timer_mig = setInterval(migalka, 500, lines_sort[key]);
    		return;
    	}
    }
}

function change_length(line, length, text_point_index)
{
	var rez_lines = line_on_lines(line);
	var oldLength = line.length;
	var Dx, Dy, can_ver, coef, smez_wall0, smez_wall1, line2, line3, cp1, cp2, point_s, point_sn, point_s2, point_s3, newPoint, 
		point_s_old, point_s2_old, smes, lx, ly;

	for (var key in rez_lines)
	{
		if (point_ravny(line.segments[0].point, rez_lines[key].segments[0].point)
			|| point_ravny(line.segments[0].point, rez_lines[key].segments[1].point))
		{
			smez_wall0 = rez_lines[key];
		}
		if (point_ravny(line.segments[1].point, rez_lines[key].segments[0].point)
			|| point_ravny(line.segments[1].point, rez_lines[key].segments[1].point))
		{
			smez_wall1 = rez_lines[key];
		}
	}
	if (!fixed_walls[smez_wall0.id] && !fixed_walls[smez_wall1.id])
	{
		if (line.segments[0].point.x > line.segments[1].point.x)
		{
			line2 = smez_wall0;
			point_s = line.segments[0].point;
			point_sn = line.segments[1].point;
		}
		else
		{
			line2 = smez_wall1;
			point_s = line.segments[1].point;
			point_sn = line.segments[0].point;
		}
	}
	else if (!fixed_walls[smez_wall0.id])
	{
		line2 = smez_wall0;
		point_s = line.segments[0].point;
		point_sn = line.segments[1].point;
	}
	else if (!fixed_walls[smez_wall1.id])
	{
		line2 = smez_wall1;
		point_s = line.segments[1].point;
		point_sn = line.segments[0].point;
	}
	else
	{
		//треугольник

		line2 = smez_wall0;
		point_s = line.segments[0].point;
		cp1 = line.segments[1].point;
		
		for (var i in line2.segments)
		{
			if (!point_ravny(line2.segments[i].point, point_s))
			{
				cp2 = line2.segments[i].point;
			}
		}

	   	var intersections = circle_intersections(cp1.x, cp1.y, +length, cp2.x, cp2.y, line2.length);

	   	if (!intersections)
	   	{
	   		line2 = smez_wall1;
			point_s = line.segments[1].point;
			cp1 = line.segments[0].point;
			
			for (var i in line2.segments)
			{
				if (!point_ravny(line2.segments[i].point, point_s))
				{
					cp2 = line2.segments[i].point;
				}
			}

		   	intersections = circle_intersections(cp1.x, cp1.y, +length, cp2.x, cp2.y, line2.length);
		   	if (!intersections)
		   	{
		   		alert('Недопустимая длина');
		   		delete cansel_array[count_cansel];
		   		count_cansel--;
		   		return;
		   	}
	   	}

	   	var len_0 = Path.Line(point_s, intersections[0]);
	   	var len_1 = Path.Line(point_s, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newPoint = intersections[0];
	   	}
	   	else
	   	{
	   		newPoint = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

		can_ver = moveVertexName(line, line2, newPoint);

		cansel_array[count_cansel].vert[0] = can_ver;
	    cansel_array[count_cansel].vert[1] = new Point(point_s.x - 10, point_s.y - 5);
	    cansel_array[count_cansel].vert[2] = can_ver;
	    cansel_array[count_cansel].vert[3] = new Point(point_s.x - 10, point_s.y - 5);

		points = [cp1.clone(), point_s.clone()];
	    cansel_array[count_cansel].line_id[line.id] = points;
	    points = [point_s.clone(), cp2.clone()];
	    cansel_array[count_cansel].line_id[line2.id] = points;

        line.removeSegments();
	    line.addSegments([cp1, newPoint]);
	    line2.removeSegments();
	    line2.addSegments([newPoint, cp2]);
	    fixed_walls[line.id] = true;
	    add_text_v_or_h(line.id);
	    add_text_v_or_h(line2.id);

		return;
	}

	rez_lines = line_on_lines(line2);
	for (var key in rez_lines)
	{
		if ((point_ravny(line2.segments[0].point, rez_lines[key].segments[0].point)
			|| point_ravny(line2.segments[0].point, rez_lines[key].segments[1].point)) 
			&& !point_ravny(line2.segments[0].point, point_s))
		{
			line3 = rez_lines[key];
			point_s2 = line2.segments[0].point;
		}
		if ((point_ravny(line2.segments[1].point, rez_lines[key].segments[0].point)
			|| point_ravny(line2.segments[1].point, rez_lines[key].segments[1].point)) 
			&& !point_ravny(line2.segments[1].point, point_s))
		{
			line3 = rez_lines[key];
			point_s2 = line2.segments[1].point;
		}
	}

	if (!point_ravny(line3.segments[0].point, point_s2))
	{
		point_s3 = line3.segments[0].point;
	}
	else if (!point_ravny(line3.segments[1].point, point_s2))
	{
		point_s3 = line3.segments[1].point;
	}

	point_s_old = point_s;
	point_s2_old = point_s2;

	//косая

	if (line_h_or_v(line) === null)
	{
		if (!point_ravny(point_s, line.segments[0].point))
		{
			cp1 = line.segments[0].point;
		}
		if (!point_ravny(point_s, line.segments[1].point))
		{
			cp1 = line.segments[1].point;
		}
		if (!point_ravny(point_s, line2.segments[0].point))
		{
			cp2 = line2.segments[0].point;
		}
		if (!point_ravny(point_s, line2.segments[1].point))
		{
			cp2 = line2.segments[1].point;
		}

		coef = coef_wall_kos[line.id];

		if (coef === undefined)
		{
			razv_wall_kos[line.id] = {p1: cp1.clone(), p2: point_s.clone()};
			var chis = new Decimal(new Decimal(line.segments[1].point.y).minus(line.segments[0].point.y)), 
				znam = new Decimal(new Decimal(line.segments[1].point.x).minus(line.segments[0].point.x));
	    	coef = chis.dividedBy(znam);
		}

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
    	Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

    	if (razv_wall_kos[line.id].p1.x > razv_wall_kos[line.id].p2.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (razv_wall_kos[line.id].p1.y > razv_wall_kos[line.id].p2.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}

    	newPoint = new Point(new Decimal(cp1.x).plus(Dx).toNumber(), new Decimal(cp1.y).plus(Dy).toNumber());

		cansel_array[count_cansel].vert[0] = +text_point_index + 1;
	    cansel_array[count_cansel].vert[1] = new Point(point_s.x - 10, point_s.y - 5);
	    if (text_points[+text_point_index + 2] !== undefined)
	    {
	    	cansel_array[count_cansel].vert[2] = +text_point_index + 2;
	    }
	    else
	    {
	    	cansel_array[count_cansel].vert[2] = 0;
	    }
	    cansel_array[count_cansel].vert[3] = new Point(cp2.x - 10, cp2.y - 5);

		text_points[+text_point_index + 1].point = new Point(newPoint.x - 10, newPoint.y - 5);


		points = [cp1.clone(), point_s.clone()];
	    cansel_array[count_cansel].line_id[line.id] = points;
	    points = [point_s.clone(), cp2.clone()];
	    cansel_array[count_cansel].line_id[line2.id] = points;
	    
	    if (line_h_or_v(line2) === 'v' && !fixed_walls[line3.id])
	    {
	    	if (line_h_or_v(line3) === null)
	    	{
	    		var chis = new Decimal(new Decimal(line3.segments[1].point.y).minus(line3.segments[0].point.y)), 
					znam = new Decimal(new Decimal(line3.segments[1].point.x).minus(line3.segments[0].point.x));
		    	coef_wall_kos[line3.id] = chis.dividedBy(znam);
		    	if (point_ravny(cp2, line3.segments[0].point))
		    	{
			    	razv_wall_kos[line3.id] = {p1: line3.segments[0].point.clone(), p2: line3.segments[1].point.clone()};
			    }
			    else
			    {
			    	razv_wall_kos[line3.id] = {p1: line3.segments[1].point.clone(), p2: line3.segments[0].point.clone()};
			    }
	    	}
	    	cp2 = new Point(newPoint.x, cp2.y);
	    }
	    else if (line_h_or_v(line2) === 'h' && !fixed_walls[line3.id])
	    {
	    	if (line_h_or_v(line3) === null)
	    	{
	    		var chis = new Decimal(new Decimal(line3.segments[1].point.y).minus(line3.segments[0].point.y)), 
					znam = new Decimal(new Decimal(line3.segments[1].point.x).minus(line3.segments[0].point.x));
		    	coef_wall_kos[line3.id] = chis.dividedBy(znam);
		    	if (point_ravny(cp2, line3.segments[0].point))
		    	{
			    	razv_wall_kos[line3.id] = {p1: line3.segments[0].point.clone(), p2: line3.segments[1].point.clone()};
			    }
			    else
			    {
			    	razv_wall_kos[line3.id] = {p1: line3.segments[1].point.clone(), p2: line3.segments[0].point.clone()};
			    }
	    	}
	    	cp2 = new Point(cp2.x, newPoint.y);
	    }
	    else
	    {
	    	var chis = new Decimal(new Decimal(line2.segments[1].point.y).minus(line2.segments[0].point.y)), 
				znam = new Decimal(new Decimal(line2.segments[1].point.x).minus(line2.segments[0].point.x));
	    	coef_wall_kos[line2.id] = chis.dividedBy(znam);
	    	razv_wall_kos[line2.id] = {p1: point_s.clone(), p2: cp2.clone()};
	    }

	    if (cp2.x > point_s.x && cp2.x < newPoint.x 
    	|| cp2.y > point_s.y && cp2.y < newPoint.y 
    	|| cp2.x < point_s.x && cp2.x > newPoint.x 
    	|| cp2.y < point_s.y && cp2.y > newPoint.y)
	    {
	    	razv_wall[line2.id] = true;
	    	cansel_array[count_cansel].razv_walls.push(line2.id);
	    }
	    if (cp2.x > point_s3.x && point_s2_old.x < point_s3.x 
    	|| cp2.y > point_s3.y && point_s2_old.y < point_s3.y 
    	|| cp2.x < point_s3.x && point_s2_old.x > point_s3.x 
    	|| cp2.y < point_s3.y && point_s2_old.y > point_s3.y)
	    {
	    	razv_wall[line3.id] = true;
	    	cansel_array[count_cansel].razv_walls.push(line3.id);
	    }

        line.removeSegments();
	    line.addSegments([cp1, newPoint]);
	    line2.removeSegments();
	    line2.addSegments([newPoint, cp2]);
	    fixed_walls[line.id] = true;
	    add_text_v_or_h(line.id);
	    add_text_v_or_h(line2.id);
	    if (line_h_or_v(line2) !== null && !fixed_walls[line3.id])
	    {
	    	points = [point_s2.clone(), point_s3.clone()];
	    	cansel_array[count_cansel].line_id[line3.id] = points;
		    text_points[+text_point_index + 2].point = new Point(cp2.x - 10, cp2.y - 5);
	    	line3.removeSegments();
	    	line3.addSegments([cp2, point_s3]);
	    	add_text_v_or_h(line3.id);
	    }
		return;
	}


	var chis = new Decimal(new Decimal(line.segments[1].point.y).minus(line.segments[0].point.y)), 
		znam = new Decimal(new Decimal(line.segments[1].point.x).minus(line.segments[0].point.x));
	coef = chis.dividedBy(znam);

	var dec_length = new Decimal(length).minus(line.length);

	Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
	Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();


	if (point_s.y < point_sn.y || point_s.x < point_sn.x)
	{
		smes = '<';
	}
	else if (point_s.y > point_sn.y || point_s.x > point_sn.x)
	{
		smes = '>';
	}

	lx = 0;
	ly = 0;
	if (razv_wall[line.id])
	{
    	if (point_sn.x === point_s.x)
		{
			ly = line.length;
			lx = 0;
		}
		else if (point_sn.y === point_s.y)
		{
			lx = line.length;
			ly = 0;
		}
		dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
		Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();
		if (smes === '<')
		{
			smes = '>';
		}
		else if (smes === '>')
		{
			smes = '<';
		}
	}

	if (smes === '>')
	{
		point_s = new Point(new Decimal(point_s.x).plus(Dx).plus(lx).toNumber(), new Decimal(point_s.y).plus(Dy).plus(ly).toNumber());
		point_s2 = new Point(new Decimal(point_s2.x).plus(Dx).plus(lx).toNumber(), new Decimal(point_s2.y).plus(Dy).plus(ly).toNumber());
    }
    else if (smes === '<')
    {
    	point_s = new Point(new Decimal(point_s.x).minus(Dx).minus(lx).toNumber(), new Decimal(point_s.y).minus(Dy).minus(ly).toNumber());
		point_s2 = new Point(new Decimal(point_s2.x).minus(Dx).minus(lx).toNumber(), new Decimal(point_s2.y).minus(Dy).minus(ly).toNumber());
    }

    if (point_s2.x > point_s3.x && point_s2_old.x < point_s3.x 
    	|| point_s2.y > point_s3.y && point_s2_old.y < point_s3.y 
    	|| point_s2.x < point_s3.x && point_s2_old.x > point_s3.x 
    	|| point_s2.y < point_s3.y && point_s2_old.y > point_s3.y)
    {
    	razv_wall[line3.id] = true;
    	cansel_array[count_cansel].razv_walls.push(line3.id);
    }

    cansel_array[count_cansel].vert[0] = +text_point_index + 1;
    cansel_array[count_cansel].vert[1] = new Point(point_s_old.x - 10, point_s_old.y - 5);

    text_points[+text_point_index + 1].point = new Point(point_s.x - 10, point_s.y - 5);

    points = [point_sn.clone(), point_s_old.clone()];
	cansel_array[count_cansel].line_id[line.id] = points;
	points = [point_s_old.clone(), point_s2_old.clone()];
	cansel_array[count_cansel].line_id[line2.id] = points;

    line.removeSegments();
	line.addSegments([point_sn, point_s]);
    if (!fixed_walls[line3.id] && line_h_or_v(line2) !== null)
    {
	    points = [point_s2_old.clone(), point_s3.clone()];
	    cansel_array[count_cansel].line_id[line3.id] = points;

	    cansel_array[count_cansel].vert[2] = +text_point_index + 2;
    	cansel_array[count_cansel].vert[3] = new Point(point_s2_old.x - 10, point_s2_old.y - 5);
    	text_points[+text_point_index + 2].point = new Point(point_s2.x - 10, point_s2.y - 5);

	    line2.removeSegments();
	    line2.addSegments([point_s, point_s2]);

	    var chis = new Decimal(new Decimal(line3.segments[1].point.y).minus(line3.segments[0].point.y)), 
				znam = new Decimal(new Decimal(line3.segments[1].point.x).minus(line3.segments[0].point.x));
	    coef_wall_kos[line3.id] = chis.dividedBy(znam);
	    razv_wall_kos[line3.id] = {p1: point_s2_old.clone(), p2: point_s3.clone()};

	    line3.removeSegments();
	    line3.addSegments([point_s2, point_s3]);
	    fixed_walls[line.id] = true;
	    add_text_v_or_h(line.id);
	    add_text_v_or_h(line2.id);
	    add_text_v_or_h(line3.id);
	}
	else
	{
		cansel_array[count_cansel].vert[2] = +text_point_index + 1;
    	cansel_array[count_cansel].vert[3] = new Point(point_s_old.x - 10, point_s_old.y - 5);
    	if (coef_wall_kos[line2.id] === undefined)
    	{
    		var chis = new Decimal(new Decimal(line2.segments[1].point.y).minus(line2.segments[0].point.y)), 
				znam = new Decimal(new Decimal(line2.segments[1].point.x).minus(line2.segments[0].point.x));
	    	coef_wall_kos[line2.id] = chis.dividedBy(znam);
    		razv_wall_kos[line2.id] = {p1: point_s_old.clone(), p2: point_s2_old.clone()};
		}
	    line2.removeSegments();
	    line2.addSegments([point_s, point_s2_old]);
	    fixed_walls[line.id] = true;
	    add_text_v_or_h(line.id);
	    add_text_v_or_h(line2.id);
	}
}

function triangulator()
{
	g_points = getPathsPoints(lines_sort);
	g_points = changePointsOrderForNaming(g_points, 2);
	var ctx1, ctx2, ctx3, d, kolvo_lines, kolvo_lines_2, kolvo_lines_4a, kolvo_lines_4b, kolvo_lines_2_y, kolvo_lines_4a_y, kolvo_lines_4b_y,
		cx, cy, cp2, c0, chek_line_2, cp4a, chek_line_4a, cp4b, chek_line_4b, chek_line_2_y, chek_line_4a_y, chek_line_4b_y, bdel;
	var vertices = [];
	for (var key = g_points.length; key--;)
	{
		vertices[key] = [g_points[key].x, g_points[key].y];
	}
	
	var triangles_points = Delaunay.triangulate(vertices);

	for(var i = triangles_points.length; i;)
	{
		--i; ctx1 = new Point(vertices[triangles_points[i]][0], vertices[triangles_points[i]][1]);
		--i; ctx2 = new Point(vertices[triangles_points[i]][0], vertices[triangles_points[i]][1]);
		--i; ctx3 = new Point(vertices[triangles_points[i]][0], vertices[triangles_points[i]][1]);
		d = Path.Line(ctx1, ctx2);
		d.strokeColor = 'black';
		d.strokeWidth = 1;
		diag.push(d);
		d = Path.Line(ctx2, ctx3);
		d.strokeColor = 'black';
		d.strokeWidth = 1;
		diag.push(d);
	}

	for (var i = diag.length; i--;)
	{
		if (!good_diag(diag[i]))
		{
			diag[i].remove();
			diag.splice(i, 1);
		}
	}
}


function triangles_count()
{
	var count = 0, op, tr_bool;
	for (var i in diag)
	{
		var hitResults0 = project.hitTestAll(diag[i].segments[0].point, {class: Path, segments: true, tolerance: 2});
		var hitResults1 = project.hitTestAll(diag[i].segments[1].point, {class: Path, segments: true, tolerance: 2});
		for (var key0 = hitResults0.length; key0--;)
		{
			if (lines_ravny(hitResults0[key0].item, diag[i]) || hitResults0[key0].item.segments.length !== 2)
			{
				continue;
			}
			for (var key1 = hitResults1.length; key1--;)
			{
				if (!lines_ravny(hitResults1[key1].item, diag[i]) && hitResults1[key1].item.segments.length === 2)
				{
					op = obshaya_point(hitResults0[key0].item, hitResults1[key1].item);
					if (op !== null)
					{
						var path1 = new CompoundPath({
						    children: [
						        diag[i].clone(),
						        hitResults0[key0].item.clone(),
						        hitResults1[key1].item.clone()
						    ]
						});
						tr_bool = false;
						for (var j = triangles.length; j--;)
						{
							if (path1.compare(triangles[j]))
							{
								tr_bool = true;
								break;
							}
						}
						if (!tr_bool)
						{
							triangles.push(path1);
						}
						else
						{
							path1.remove();
						}
					}
				}
			}
		}
		hitResults0.splice(0, hitResults0.length);
		hitResults1.splice(0, hitResults1.length);
	}
	for (var key = triangles.length; key--;)
	{
		triangles[key].remove();
		count++;
	}
	triangles = [];
	return count;
}


function pulemet()
{
	var count = 0, op, d;
	for (var i in diag)
	{
		var hitResults0 = project.hitTestAll(diag[i].segments[0].point, {class: Path, segments: true, tolerance: 2});
		var hitResults1 = project.hitTestAll(diag[i].segments[1].point, {class: Path, segments: true, tolerance: 2});
		count = 0;
		for (var key0 = hitResults0.length; key0--;)
		{
			if (lines_ravny(hitResults0[key0].item, diag[i]) || hitResults0[key0].item.segments.length !== 2)
			{
				continue;
			}
			for (var key1 = hitResults1.length; key1--;)
			{
				if (!lines_ravny(hitResults1[key1].item, diag[i]) && hitResults1[key1].item.segments.length === 2)
				{
					op = obshaya_point(hitResults0[key0].item, hitResults1[key1].item);
					if (op !== null)
					{
						count++;
					}
				}
			}
		}

		if (count !== 2)
		{
			for (var key = g_points.length; key--;)
			{
				if (!point_ravny(diag[i].segments[0].point, g_points[key]))
				{
					d = Path.Line(diag[i].segments[0].point, g_points[key]);
					if (good_diag(d))
					{
						d.strokeColor = 'black';
						d.strokeWidth = 1;
						diag.push(d);
						break;
					}
					else
					{
						d.remove();
					}
				}
			}
		}
	}
}


function good_diag(diag_i)
{
	var chertezh = new Path();
	for (var key = g_points.length; key--;)
	{
		chertezh.add(g_points[key]);
	}

	chertezh.closed = true;

	if (!chertezh.contains(diag_i.position))
	{
		chertezh.remove();
		project.activeLayer.addChildren(lines_sort);
		return false;
	}

	chertezh.remove();
	project.activeLayer.addChildren(lines_sort);

	for (var key = lines_sort.length; key--;)
	{
		if (isIntersect(diag_i.segments[0].point, diag_i.segments[1].point, lines_sort[key].segments[0].point, lines_sort[key].segments[1].point))
		{
			return false;
		}
		if (diag_i.contains(lines_sort[key].segments[0].point) && diag_i.contains(lines_sort[key].segments[1].point))
		{
			return false;
		}
		if (lines_sort[key].contains(diag_i.segments[0].point) && lines_sort[key].contains(diag_i.segments[1].point))
		{
			return false;
		}
		if (point_ravny(lines_sort[key].segments[0].point, diag_i.segments[0].point) 
			&& point_ravny(lines_sort[key].segments[1].point, diag_i.segments[1].point) 
			|| point_ravny(lines_sort[key].segments[0].point, diag_i.segments[1].point) 
			&& point_ravny(lines_sort[key].segments[1].point, diag_i.segments[0].point))
		{
			return false;
		}
	}

	for (var key in diag)
	{
		if (diag_i === diag[key])
		{
			continue;
		}
		if (isIntersect(diag_i.segments[0].point, diag_i.segments[1].point, 
						diag[key].segments[0].point, diag[key].segments[1].point))
		{
			return false;
		}
		if (diag_i.contains(diag[key].segments[0].point) && diag_i.contains(diag[key].segments[1].point))
		{
			return false;
		}
		if (diag[key].contains(diag_i.segments[0].point) && diag[key].contains(diag_i.segments[1].point))
		{
			return false;
		}
		if (point_ravny(diag[key].segments[0].point, diag_i.segments[0].point) 
			&& point_ravny(diag[key].segments[1].point, diag_i.segments[1].point) 
			|| point_ravny(diag[key].segments[0].point, diag_i.segments[1].point) 
			&& point_ravny(diag[key].segments[1].point, diag_i.segments[0].point))
		{
			return false;
		}
	}

	return true;
}


function square()
{
	var a = 0, b = 0, c = 0, p = 0, s = 0, sq = 0, op, tr_bool;
	for (var i = diag_sort.length; i--;)
	{
		var hitResults0 = project.hitTestAll(diag_sort[i].segments[0].point, {class: Path, segments: true, tolerance: 2});
		var hitResults1 = project.hitTestAll(diag_sort[i].segments[1].point, {class: Path, segments: true, tolerance: 2});
		for (var key0 = hitResults0.length; key0--;)
		{
			if (lines_ravny(hitResults0[key0].item, diag_sort[i]) || hitResults0[key0].item.segments.length !== 2)
			{
				continue;
			}
			for (var key1 = hitResults1.length; key1--;)
			{
				if (!lines_ravny(hitResults1[key1].item, diag_sort[i]) && hitResults1[key1].item.segments.length === 2)
				{
					op = obshaya_point(hitResults0[key0].item, hitResults1[key1].item);
					if (op !== null)
					{
						var path1 = new CompoundPath({
						    children: [
						        diag_sort[i].clone(),
						        hitResults0[key0].item.clone(),
						        hitResults1[key1].item.clone()
						    ],
						    diag_length: text_diag[i].content
						});
						tr_bool = false;
						for (var j = triangles.length; j--;)
						{
							if (path1.compare(triangles[j]))
							{
								tr_bool = true;
								break;
							}
						}
						if (!tr_bool)
						{
							triangles.push(path1);
						}
						else
						{
							path1.remove();
						}
					}
				}
			}
		}
		hitResults0.splice(0, hitResults0.length);
		hitResults1.splice(0, hitResults1.length);
	}

	for (var key = triangles.length; key--;)
	{
		a = parseFloat(triangles[key].diag_length);
		b = triangles[key].children[1].length;
		c = triangles[key].children[2].length;
		p = (a + b + c) / 2;
		s = Math.sqrt(p*(p-a)*(p-b)*(p-c));
		sq += s;
	}
	sq = sq / 10000;
	elem_jform_n4.value = sq.toFixed(2);
}

function perimetr()
{
	var p = 0;
	for (var key in lines)
	{
		p += lines[key].length;
	}
	p = p / 100;
	elem_jform_n5.value = p.toFixed(2);
}

function align_center()
{
	var chertezh = new CompoundPath();

	chertezh.addChildren(project.activeLayer.children);
	project.activeLayer.removeChildren();
	
	chertezh.position=view.center;

	project.activeLayer.addChildren(chertezh.children);
	chertezh.remove();
}

function zerkalo(p_u)
{
	var chertezh = new CompoundPath();

	chertezh.addChildren(lines_sort);
	chertezh.addChildren(diag_sort);
	for (var i = polotna.length; i--;)
	{
		if (polotna[i].up !== undefined)
		{
			chertezh.addChild(polotna[i].up);
		}
		if (polotna[i].down !== undefined)
		{
			chertezh.addChild(polotna[i].down);
		}
	}
	chertezh.scale(-1, 1);

	project.activeLayer.removeChildren();

	project.activeLayer.addChildren(lines_sort);
	project.activeLayer.addChildren(diag_sort);
	project.activeLayer.addChildren(text_points);
	for (var i = polotna.length; i--;)
	{
		if (polotna[i].up !== undefined)
		{
			project.activeLayer.addChild(polotna[i].up);
		}
		if (polotna[i].down !== undefined)
		{
			project.activeLayer.addChild(polotna[i].down);
		}
	}

	chertezh.remove();

	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
		lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
		lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
		lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
		diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
		diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
		diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();

	for (var key = 0; key < lines_sort.length; key++)
    {
    	add_text_v_or_h(lines_sort[key].id);
    	if (p_u === 1)
    	{
    		text_lines[lines_sort[key].id].content = (+text_lines[lines_sort[key].id].content);
    	}
    	else
    	{
	    	text_lines[lines_sort[key].id].content = (+text_lines[lines_sort[key].id].content * p_u).toFixed(1);
	    }
    }

    for (var key = 0; key < diag_sort.length; key++)
    {
    	add_text_diag(key);
    	if (p_u === 1)
    	{
    		text_diag[key].content = (+text_diag[key].content);
    	}
	    else
	    {
	    	text_diag[key].content = (+text_diag[key].content * p_u).toFixed(1);
	    }
    }
    
    var j = 0;
    for (var i = 0; i < lines_sort.length; i++)
    {
    	if (i === 0)
    	{
    		j = lines_sort.length - 1;
    	}
    	else
    	{
    		j = i - 1;
    	}
    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
    	{
    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
    	}
    }

}

function remove_none_shvy()
{
	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
		lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
		lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
		lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
		diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
		diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
		diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();
	var points_m = findMinAndMaxCordinate_g();

	for (var i = polotna.length; i--;)
	{
		polotna[i].left.remove();
		polotna[i].right.remove();
		
		delete polotna[i].left;
		delete polotna[i].right;

		if (polotna[i].down.position.y > points_m.maxY - 1)
		{
			polotna[i].down.remove();
			delete polotna[i].down;
		}
		if (polotna[i].up.position.y < points_m.minY + 1)
		{
			polotna[i].up.remove();
			delete polotna[i].up;
		}
	}

	for (var i = polotna.length; i--;)
	{
		for (var j = polotna.length; j--;)
		{
			if (polotna[i].up === undefined || polotna[j].down === undefined)
			{
				continue;
			}
			if (polotna[i].up.intersects(polotna[j].down) && polotna[i].up.length > polotna[j].down.length)
			{
				polotna[i].up.remove();
				delete polotna[i].up;
			}
		}

		for (var j = polotna.length; j--;)
		{
			if (polotna[i].down === undefined || polotna[j].up === undefined)
			{
				continue;
			}
			if (polotna[i].down.intersects(polotna[j].up) && polotna[i].down.length > polotna[j].up.length)
			{
				polotna[i].down.remove();
				delete polotna[i].down;
			}
		}
	}

	var chertezh = new Path();
	for (var key = g_points.length; key--;)
	{
		chertezh.add(g_points[key].clone());
	}
	chertezh.closed = true;

	for (var i = polotna.length; i--;)
	{
		if (polotna[i].down !== undefined)
		{
			if (!polotna[i].down.intersects(chertezh) && !chertezh.contains(polotna[i].down.position))
			{
				polotna[i].down.remove();
				delete polotna[i].down;
			}
		}
		if (polotna[i].up !== undefined)
		{
			if (!polotna[i].up.intersects(chertezh) && !chertezh.contains(polotna[i].up.position))
			{
				polotna[i].up.remove();
				delete polotna[i].up;
			}
		}
	}
	chertezh.remove();
}

function rotate_final()
{
	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].rotate(angle_final, view.center);

		lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
		lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
		lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
		lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].rotate(angle_final, view.center);

		diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
		diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
		diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
		diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();
	for (var i = polotna.length; i--;)
	{
		if (polotna[i].up !== undefined)
		{
			polotna[i].up.rotate(angle_final, view.center);
			polotna[i].up.strokeWidth = 2;
		}
		if (polotna[i].down !== undefined)
		{
			polotna[i].down.rotate(angle_final, view.center);
			polotna[i].down.strokeWidth = 2;
		}
	}
}

function remove_pt_intersects()
{
	var del_bool = true;
	for (var i = text_points.length; i--;)
	{
		del_bool = true;
		for (var j = text_points_lines_id.length; j--;)
		{
			if (text_points[i].id === +text_points_lines_id[j].id_pt)
			{
				del_bool = false;
				break;
			}
		}
		if (del_bool)
		{
			text_points[i].remove();
		}
	}
}

var polotna = [], points_poloten = [], koordinats_poloten = [], kolvo_polos = 1;

function polotno_final(gradus_f, j_f, kolvo_poloten, p_usadki)
{
	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].rotate(gradus_f, view.center);

		lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
		lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
		lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
		lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].rotate(gradus_f, view.center);

		diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
		diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
		diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
		diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();
	angle_final = -gradus_f;

	add_polotno(width_polotna[j_f].width, p_usadki, kolvo_poloten);

	for (var key = lines_sort.length; key--;)
    {
    	add_text_v_or_h(lines_sort[key].id);
    	text_lines[lines_sort[key].id].content = (+text_lines[lines_sort[key].id].content * p_usadki).toFixed(1);
    }

    for (var key = diag_sort.length; key--;)
    {
    	add_text_diag(key);
    	text_diag[key].content = (+text_diag[key].content * p_usadki).toFixed(1);
    }
    
    var j = 0;
    for (var i = 0; i < lines_sort.length; i++)
    {
    	if (i === 0)
    	{
    		j = lines_sort.length - 1;
    	}
    	else
    	{
    		j = i - 1;
    	}
    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
    	{
    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
    	}
    }


    //console.log((new Decimal(1).minus(p_usadki)).times(100).toNumber(), '%');

	get_koordinats_poloten(p_usadki);

	var sq_og = parseFloat(elem_jform_n4.value);
	var sq2 = new Decimal(sq_og).times(p_usadki).times(p_usadki).toNumber();
	var sq1 = 0;
	var price_it, pr_dec, sq1_dec;

	for (var key = polotna.length; key--;)
	{
		sq1_dec = new Decimal(polotna[key].down.length).times(polotna[key].left.length);
		sq1_dec = sq1_dec.dividedBy(10000);
		sq1_dec = sq1_dec.times(p_usadki).times(p_usadki);
		sq1 = new Decimal(sq1).plus(sq1_dec).toNumber();
		//sq1 += ((polotna[key].down.length * polotna[key].left.length) / 10000) * p_usadki * p_usadki;
	}

	var sq_obr = new Decimal(sq1).minus(sq2).toNumber();
	sq_obr = +sq_obr.toFixed(2);
	//console.log(sq_obr, "sq_obr");

	price_it = new Decimal(sq_og).times(width_polotna[j_f].price).toNumber();
	if (sq_obr > new Decimal(sq_og).times(0.5).toNumber())
	{
		pr_dec = new Decimal(sq_obr).times(new Decimal(width_polotna[j_f].price).times(0.4));
		price_it = new Decimal(price_it).plus(pr_dec).toNumber();
		square_obrezkov = sq_obr;
	}
	//console.log(price_it, "price");

	width_final = width_polotna[j_f].width;
	
    AndroidFunction.func_elem_jform_width(width_final);
	//console.log(width_final, "width_polotna");

	for (var i = polotna.length; i--;)
	{
		polotna[i].up.strokeColor = 'red';
		polotna[i].up.dashArray = [10, 4];
		polotna[i].down.strokeColor = 'red';
		polotna[i].down.dashArray = [10, 4];
		polotna[i].left.strokeColor = 'red';
		polotna[i].left.dashArray = [10, 4];
		polotna[i].right.strokeColor = 'red';
		polotna[i].right.dashArray = [10, 4];
	}

}

var polotno = function()
{
	var gradus_f, j_f, break_bool = false, points_m, kolvo_poloten = 1, height_chert, j_n, p_usadki, h, hp;
	var sq_og = parseFloat(elem_jform_n4.value);
	var price_it, sq_obr, sq_min, price_min, sq1, sq1_dec, usadka_final;
	var time_end, time_start = performance.now();

	while (true)
	{
		sq_min = 100222;
		price_min = 10222444;
		usadka_final = 0;
		for (var i = lines_sort.length; i--;)
		{
			lines_sort[i].rotate(-1, view.center);

			lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
			lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
			lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
			lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
		}
		for (var i = diag_sort.length; i--;)
		{
			diag_sort[i].rotate(-1, view.center);

			diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
			diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
			diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
			diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
		}
		okruglenie_all_segments();

		for (var angle_rotate = 0; angle_rotate < 181; angle_rotate++)
		{
			for (var i = lines_sort.length; i--;)
			{
				lines_sort[i].rotate(1, view.center);

				lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
				lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
				lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
				lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
			}
			for (var i = diag_sort.length; i--;)
			{
				diag_sort[i].rotate(1, view.center);

				diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
				diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
				diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
				diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
			}
			okruglenie_all_segments();

			points_m = findMinAndMaxCordinate_g();

			h = points_m.maxY - points_m.minY;
			height_chert = (h * 0.92) / kolvo_poloten;
			j_n = width_polotna.length - 1;


			for (var j = 0; j < width_polotna.length; j++)
			{
				if (width_polotna[j].width < height_chert)
				{
					j_n = j;
					break;
				}
			}

			for (var j = j_n + 1; j--;)
			{
				hp = new Decimal(kolvo_poloten).times(width_polotna[j].width);
				p_usadki = new Decimal(h).minus(hp);
				p_usadki = p_usadki.times(100);
				p_usadki = p_usadki.dividedBy(h);
				p_usadki = p_usadki.times(10).toNumber();
				p_usadki = Math.ceil(p_usadki);
				p_usadki = new Decimal(p_usadki).dividedBy(10);
				p_usadki = new Decimal(100).minus(p_usadki);
				p_usadki = p_usadki.dividedBy(100);
				p_usadki = p_usadki.minus(0.001).toNumber();
				if (p_usadki > 0.92)
				{
					p_usadki = 0.92;
				}
				else if (p_usadki < 0.91)
				{
					continue;
				}

				if (kolvo_poloten == 1)
				{

					if (add_polotno_fast(width_polotna[j].width, p_usadki) === false)
					{
						continue;
					}
				}
				else
				{
					if (add_polotno(width_polotna[j].width, p_usadki, kolvo_poloten) === false)
					{
						continue;
					}
				}

				if (kolvo_polos == kolvo_poloten)
				{
					sq1 = 0;

					for (var key = polotna.length; key--;)
					{
						sq1_dec = new Decimal(polotna[key].down.length).times(polotna[key].left.length);
						sq1_dec = sq1_dec.dividedBy(10000);
						sq1 = new Decimal(sq1).plus(sq1_dec).toNumber();
					}
					sq1 = +sq1.toFixed(2);
					sq_obr = new Decimal(sq1).minus(sq_og).toNumber();

					if ((width_polotna[j].price < price_min) || (width_polotna[j].price === price_min && sq_obr < sq_min))
					{
						price_min = width_polotna[j].price;
						sq_min = sq_obr;
						gradus_f = angle_rotate;
						j_f = j;
						usadka_final = p_usadki;
						break_bool = true;
						//console.log(kolvo_poloten, price_min, sq_min, usadka_final, width_polotna[j].width, angle_rotate, sq1);
					}

					if (polotna[polotna.length - 1].up.position.y <= points_m.minY)
					{
						break;
					}
				}
			}
		}

		for (var i = lines_sort.length; i--;)
		{
			lines_sort[i].rotate(180, view.center);

			lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
			lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
			lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
			lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
		}
		for (var i = diag_sort.length; i--;)
		{
			diag_sort[i].rotate(180, view.center);

			diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
			diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
			diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
			diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
		}
		okruglenie_all_segments();

		if (break_bool)
		{
			break;
		}
		kolvo_poloten++;
	}

	polotno_final(gradus_f, j_f, kolvo_poloten, usadka_final);

	time_end = performance.now() - time_start;
	//console.log(time_end, 'all_time');
};

function add_polotno_fast(p_width, p_usadki)
{

	for (var key = polotna.length; key--;)
	{
		polotna[key].left.remove();
		polotna[key].right.remove();
		polotna[key].up.remove();
		polotna[key].down.remove();
	}

	polotna = [];
	var dotyanem = new Decimal(p_width).times(0.05).toNumber();
	p_width = new Decimal(p_width).dividedBy(p_usadki).toNumber();
	//p_width = +p_width.toFixed(2);
	var points_m = findMinAndMaxCordinate_g();
	var line_left = Path.Line(new Point(points_m.minX, points_m.maxY), new Point(points_m.minX, new Decimal(points_m.maxY).minus(p_width).toNumber()));
	var line_right = Path.Line(new Point(points_m.maxX, points_m.maxY), new Point(points_m.maxX, new Decimal(points_m.maxY).minus(p_width).toNumber()));
	var line_up = Path.Line(line_left.segments[1].point, line_right.segments[1].point);
	var line_down = Path.Line(line_left.segments[0].point, line_right.segments[0].point);
	if (line_up.position.y > new Decimal(points_m.minY).plus(new Decimal(dotyanem).dividedBy(p_usadki)).toNumber())
	{
		return false;
	}
	else
	{
		polotna[0] = {up: line_up, down: line_down, left: line_left, right: line_right};
	}
}

function add_polotno(p_width, p_usadki, kolvo_poloten)
{
	for (var key = polotna.length; key--;)
	{
		polotna[key].left.remove();
		polotna[key].right.remove();
		polotna[key].up.remove();
		polotna[key].down.remove();
	}
	polotna = [];
	kolvo_polos = 0;
	var dotyanem = new Decimal(p_width).times(0.05).toNumber();
	p_width = new Decimal(p_width).dividedBy(p_usadki).toNumber();
	//p_width = +p_width.toFixed(2);
	var points_m = findMinAndMaxCordinate_g();
	var begin_point_polotna = new Point(points_m.minX, points_m.maxY);
	var chertezh = new Path();
	for (var key = g_points.length; key--;)
	{
		chertezh.add(g_points[key].clone());
	}
	chertezh.closed = true;

	var left, right, up, down, num_polotna = 0;
	while (true)
	{
		if (right !== undefined)
		{
			begin_point_polotna = new Point(right.segments[0].point.x + 1, right.segments[0].point.y);
		}
		left = find_left_polotna(begin_point_polotna, p_width, chertezh, points_m.maxX);
		if (!left)
		{
			kolvo_polos++;
			if (up.position.y <= new Decimal(points_m.minY).plus(new Decimal(dotyanem).dividedBy(p_usadki)).toNumber())
			{
				break;
			}
			begin_point_polotna = new Point(points_m.minX, up.position.y);
			left = find_left_polotna(begin_point_polotna, p_width, chertezh, points_m.maxX);
			if (!left)
			{
				break;
			}
		}
		right = find_right_polotna(new Point(left.segments[0].point.x + 5, left.segments[0].point.y), p_width, chertezh, points_m.maxX);
		up = Path.Line(left.segments[1].point, right.segments[1].point);

		down = Path.Line(left.segments[0].point, right.segments[0].point);

		polotna[num_polotna] = {up: up, down: down, left: left, right: right};
		num_polotna++;
		if (kolvo_poloten < kolvo_polos)
		{
			chertezh.remove();
			return false;
		}
	}

	chertezh.remove();
}

function find_left_polotna(begin_point_polotna, p_width, chertezh, maxX)
{
	var line_left = Path.Line(new Point(begin_point_polotna.x, new Decimal(begin_point_polotna.y).minus(0.5).toNumber()), 
						new Point(begin_point_polotna.x, new Decimal(begin_point_polotna.y).minus(p_width).plus(0.5).toNumber()));

	var sp, pos_x;
	while (line_left.position.x < maxX + 10)
	{
		sp = chertezh.contains(line_left.position);

		if (sp || line_left.intersects(chertezh))
		{
			pos_x = line_left.position.x - 2;
			line_left.removeSegments();
			line_left.addSegments([new Point(pos_x, begin_point_polotna.y), new Point(pos_x, new Decimal(begin_point_polotna.y).minus(p_width).toNumber())]);
			return line_left;
		}

		line_left.position.x += 1;
	}
	line_left.remove();
	return false;
}

function find_right_polotna(begin_point_polotna, p_width, chertezh, maxX)
{
	var line_right = Path.Line(new Point(begin_point_polotna.x, new Decimal(begin_point_polotna.y).minus(0.5).toNumber()), 
						new Point(begin_point_polotna.x, new Decimal(begin_point_polotna.y).minus(p_width).plus(0.5).toNumber()));

	var sp, pos_x;
	while (line_right.position.x < maxX + 10)
	{
		sp = chertezh.contains(line_right.position);

		if (!sp && !line_right.intersects(chertezh))
		{
			pos_x = line_right.position.x + 1;
			line_right.removeSegments();
			line_right.addSegments([new Point(pos_x, begin_point_polotna.y), new Point(pos_x, new Decimal(begin_point_polotna.y).minus(p_width).toNumber())]);
			return line_right;
		}

		line_right.position.x += 1;
	}
}

function get_koordinats_poloten(p_usadki)
{
	var op, cir, intersections, pt, pt_name, intersections_line, continue_bool = false, inarr = false;
	for (var i = polotna.length; i--;)
	{
		points_poloten[i] = [];
		for (var key = text_points_lines_id.length; key--;)
		{
			op = obshaya_point(lines[+text_points_lines_id[key].id_line1], lines[+text_points_lines_id[key].id_line2]);
			if (op.x >= polotna[i].left.position.x && op.x <= polotna[i].right.position.x)
			{
				if (op.y < polotna[i].down.position.y && op.y > polotna[i].up.position.y)
				{
					for (var j = text_points.length; j--;)
					{
						if (text_points[j].id === +text_points_lines_id[key].id_pt)
						{
					    	points_poloten[i].push({point: op.clone(), name: text_points[j].content});
					    	break;
						}
					}
				}

				if (op.y === polotna[i].up.position.y)
				{
					cir = new Path.Circle(op, 2.5);
					intersections = cir.getIntersections(lines[+text_points_lines_id[key].id_line1]);
					if (intersections[0].point.y > polotna[i].up.position.y)
					{
						for (var j = text_points.length; j--;)
						{
							if (text_points[j].id === +text_points_lines_id[key].id_pt)
							{
								inarr = false;
								for (var e = points_poloten[i].length; e--;)
								{
									if (points_poloten[i][e].name === text_points[j].content)
									{
										inarr = true;
									}
								}
								if (!inarr)
								{
							    	points_poloten[i].push({point: op.clone(), name: text_points[j].content});
							    }
						    	break;
							}
						}
					}
				
					intersections = cir.getIntersections(lines[+text_points_lines_id[key].id_line2]);
					if (intersections[0].point.y > polotna[i].up.position.y)
					{
						for (var j = text_points.length; j--;)
						{
							if (text_points[j].id === +text_points_lines_id[key].id_pt)
							{
						    	inarr = false;
								for (var e = points_poloten[i].length; e--;)
								{
									if (points_poloten[i][e].name === text_points[j].content)
									{
										inarr = true;
									}
								}
								if (!inarr)
								{
							    	points_poloten[i].push({point: op.clone(), name: text_points[j].content});
							    }
						    	break;
							}
						}
					}
					cir.remove();
				}

				if (op.y === polotna[i].down.position.y)
				{
					cir = new Path.Circle(op, 2.5);
					intersections = cir.getIntersections(lines[+text_points_lines_id[key].id_line1]);
					if (intersections[0].point.y < polotna[i].down.position.y)
					{
						for (var j = text_points.length; j--;)
						{
							if (text_points[j].id === +text_points_lines_id[key].id_pt)
							{
								inarr = false;
								for (var e = points_poloten[i].length; e--;)
								{
									if (points_poloten[i][e].name === text_points[j].content)
									{
										inarr = true;
									}
								}
								if (!inarr)
								{
							    	points_poloten[i].push({point: op.clone(), name: text_points[j].content});
							    }
						    	break;
							}
						}
					}
				
					intersections = cir.getIntersections(lines[+text_points_lines_id[key].id_line2]);
					if (intersections[0].point.y < polotna[i].down.position.y)
					{
						for (var j = text_points.length; j--;)
						{
							if (text_points[j].id === +text_points_lines_id[key].id_pt)
							{
						    	inarr = false;
								for (var e = points_poloten[i].length; e--;)
								{
									if (points_poloten[i][e].name === text_points[j].content)
									{
										inarr = true;
									}
								}
								if (!inarr)
								{
							    	points_poloten[i].push({point: op.clone(), name: text_points[j].content});
							    }
						    	break;
							}
						}
					}
					cir.remove();
				}
			}
		}
	}

	for (var i = polotna.length; i--;)
	{
		continue_bool = false;
		for (var key = lines_sort.length; key--;)
		{
			if (lines_sort[key].segments[0].point.y < lines_sort[key].segments[1].point.y + 2 
				&& lines_sort[key].segments[0].point.y > lines_sort[key].segments[1].point.y - 2)
			{
				continue;
			}
			intersections_line = polotna[i].up.getIntersections(lines_sort[key]);
			if (intersections_line.length === 1)
			{
				intersections_line[0].point.x = +intersections_line[0].point.x.toFixed(2);
				intersections_line[0].point.y = +intersections_line[0].point.y.toFixed(2);
				if (intersections_line[0].point.x >= polotna[i].left.position.x
					&& intersections_line[0].point.x <= polotna[i].right.position.x)
				{
					for (var j = points_poloten.length; j--;)
					{
						if (points_poloten[j] !== undefined)
						{
							for (var p = points_poloten[j].length; p--;)
							{
								if (intersections_line[0].point.x === points_poloten[j][p].point.x 
									&& intersections_line[0].point.y === points_poloten[j][p].point.y)
								{
									continue_bool = true;
									break;
								}
							}
						}
					}
					if (continue_bool)
					{
						continue_bool = false;
						continue;
					}
					if (code === 90)
		        	{
		        		code = 64;
		        		alfavit++;
		        	}
		        	code++;
		        	if (alfavit === 0)
		        	{
			            pt_name = String.fromCharCode(code);
			        }
			        else
			        {
			        	pt_name = String.fromCharCode(code) + alfavit;
			        }
					pt = new PointText(
	                {
	                    point: new Point(intersections_line[0].point.x - 5, intersections_line[0].point.y + 15),
	                    content: pt_name,
	                    fillColor: 'blue',
	                    justification: 'center',
	                    fontFamily: 'TimesNewRoman',
	                    fontWeight: 'bold',
	                    fontSize: text_points[0].fontSize
	                });
	            	text_points.push(pt);
	            	points_poloten[i].push({point: intersections_line[0].point, name: pt_name});
	            	if (i !== polotna.length - 1)
	            	{
	            		for (var j = i + 1; j < polotna.length; j++)
	            		{
	            			if (intersections_line[0].point.x >= polotna[j].left.position.x
								&& intersections_line[0].point.x <= polotna[j].right.position.x)
	            			{
			            		points_poloten[j].push({point: intersections_line[0].point, name: pt_name});
			            		break;
			            	}
		            	}
	            	}
				}
			}
		}

		continue_bool = false;
		for (var key = lines_sort.length; key--;)
		{
			if (lines_sort[key].segments[0].point.y < lines_sort[key].segments[1].point.y + 2 
				&& lines_sort[key].segments[0].point.y > lines_sort[key].segments[1].point.y - 2)
			{
				continue;
			}
			intersections_line = polotna[i].down.getIntersections(lines_sort[key]);
			if (intersections_line.length === 1)
			{
				intersections_line[0].point.x = +intersections_line[0].point.x.toFixed(2);
				intersections_line[0].point.y = +intersections_line[0].point.y.toFixed(2);
				if (intersections_line[0].point.x >= polotna[i].left.position.x
					&& intersections_line[0].point.x <= polotna[i].right.position.x)
				{
					for (var j = points_poloten.length; j--;)
					{
						if (points_poloten[j] !== undefined)
						{
							for (var p = points_poloten[j].length; p--;)
							{
								if (intersections_line[0].point.x === points_poloten[j][p].point.x 
									&& intersections_line[0].point.y === points_poloten[j][p].point.y)
								{
									continue_bool = true;
									break;
								}
							}
						}
					}
					if (continue_bool)
					{
						continue_bool = false;
						continue;
					}
					if (code === 90)
		        	{
		        		code = 64;
		        		alfavit++;
		        	}
		        	code++;
		        	if (alfavit === 0)
		        	{
			            pt_name = String.fromCharCode(code);
			        }
			        else
			        {
			        	pt_name = String.fromCharCode(code) + alfavit;
			        }
					pt = new PointText(
	                {
	                    point: new Point(intersections_line[0].point.x - 5, intersections_line[0].point.y + 15),
	                    content: pt_name,
	                    fillColor: 'blue',
	                    justification: 'center',
	                    fontFamily: 'TimesNewRoman',
	                    fontWeight: 'bold',
	                    fontSize: text_points[0].fontSize
	                });
	            	text_points.push(pt);
	            	points_poloten[i].push({point: intersections_line[0].point, name: pt_name});
	            	if (i !== 0)
	            	{
	            		for (var j = i; j--;)
	            		{
	            			if (intersections_line[0].point.x >= polotna[j].left.position.x
								&& intersections_line[0].point.x <= polotna[j].right.position.x)
	            			{
			            		points_poloten[j].push({point: intersections_line[0].point, name: pt_name});
			            		break;
			            	}
		            	}
	            	}
				}
			}
		}
	}

	var chertezh = new Path();
	for (var key = g_points.length; key--;)
	{
		chertezh.add(g_points[key].clone());
	}
	chertezh.closed = true;

	for (var i = polotna.length; i--;)
	{
		while(!polotna[i].left.intersects(chertezh) && !chertezh.contains(polotna[i].left.position))
		{
			polotna[i].left.position.x = new Decimal(polotna[i].left.position.x).plus(0.5).toNumber();
		}
		while(!polotna[i].right.intersects(chertezh) && !chertezh.contains(polotna[i].right.position))
		{
			polotna[i].right.position.x = new Decimal(polotna[i].right.position.x).minus(0.5).toNumber();
		}
		polotna[i].up.removeSegments();
		polotna[i].up.addSegments([polotna[i].left.segments[1].point, polotna[i].right.segments[1].point]);
		polotna[i].down.removeSegments();
		polotna[i].down.addSegments([polotna[i].left.segments[0].point, polotna[i].right.segments[0].point]);
	}
	chertezh.remove();

	var kx, ky;
	for (var i = points_poloten.length; i--;)
	{
		koordinats_poloten[i] = [];
		for (var j = points_poloten[i].length; j--;)
		{
			kx = Decimal.abs(new Decimal(points_poloten[i][j].point.x).minus(polotna[i].left.position.x)).times(p_usadki).toNumber(); 
			ky = Decimal.abs(new Decimal(points_poloten[i][j].point.y).minus(polotna[i].down.position.y)).times(p_usadki).toNumber();
			kx = +kx.toFixed(1);
			ky = +ky.toFixed(1);
			koordinats_poloten[i][j] = {name: points_poloten[i][j].name, koordinats: "(" + kx + ", " + ky + ")"};
		}
	}
}

function obshaya_point(line1, line2)
{
	if (point_ravny(line1.segments[0].point, line2.segments[0].point)
		|| point_ravny(line1.segments[0].point, line2.segments[1].point))
	{
		return line1.segments[0].point.clone();
	}
	if (point_ravny(line1.segments[1].point, line2.segments[1].point)
		|| point_ravny(line1.segments[1].point, line2.segments[0].point))
	{
		return line1.segments[1].point.clone();
	}
	return null;
}

function isIntersect(point1, point2, point3, point4)// есть ли пересечения
{
    if((point1)&&(point2)&&(point3)&&(point4)) {
        var ax1 = point1.x,
            ay1 = point1.y,
            ax2 = point2.x,
            ay2 = point2.y,
            bx1 = point3.x,
            by1 = point3.y,
            bx2 = point4.x,
            by2 = point4.y,
            v1, v2, v3, v4, result;
        v1 = (bx2 - bx1) * (ay1 - by1) - (by2 - by1) * (ax1 - bx1);
        v2 = (bx2 - bx1) * (ay2 - by1) - (by2 - by1) * (ax2 - bx1);
        v3 = (ax2 - ax1) * (by1 - ay1) - (ay2 - ay1) * (bx1 - ax1);
        v4 = (ax2 - ax1) * (by2 - ay1) - (ay2 - ay1) * (bx2 - ax1);
        result = (v1 * v2 < 0) && (v3 * v4 < 0);
        return result;
    }
}

function change_length_diag_4angle(index, newLength)
{
	count_cansel++;
	cansel_array[count_cansel] = {line_id: [], vert: [], diag: [], fixed_id: diag_sort[index].id, vid: '3_1'};
	var Dx, Dy;

	var pd0 = diag_sort[index].segments[0].point.clone();
	var pd1 = diag_sort[index].segments[1].point.clone();
	var a1,b1,a2,b2,c,o,intersections,can_ver;
	var line_arr = [];
	var newPoint,newObshayaPoint1,newObshayaPoint2,oldObshayaPoint1,oldObshayaPoint2;
	c = diag_sort[index];

	for (var i in lines)
	{
		for (var j in lines)
		{
			if (obshaya_point(lines[i], lines[j]) !== null && lines[i] !== lines[j])
			{
				if (!point_ravny(obshaya_point(lines[i], lines[j]), pd0) && !point_ravny(obshaya_point(lines[i], lines[j]), pd1))
				{
					line_arr = [];
					for (var key in lines)
					{
						line_arr.push(lines[key]);
					}
					a1 = lines[i];
					b1 = lines[j];
					line_arr.splice(line_arr.indexOf(a1),1);
					line_arr.splice(line_arr.indexOf(b1),1);
				}
			}
		}
	}
	a2 = line_arr[0];
	b2 = line_arr[1];

	line_arr = [];
	if (point_ravny(a1.segments[0].point, pd0) || point_ravny(a1.segments[1].point, pd0))
	{
		o = a1;
		a1 = b1;
		b1 = o;
	}
	if (point_ravny(a2.segments[0].point, pd0) || point_ravny(a2.segments[1].point, pd0))
	{
		o = a2;
		a2 = b2;
		b2 = o;
	}

	var chis = new Decimal(new Decimal(c.segments[1].point.y).minus(c.segments[0].point.y)), 
		znam = new Decimal(new Decimal(c.segments[1].point.x).minus(c.segments[0].point.x));
	coef = chis.dividedBy(znam);

	var dec_length = new Decimal(newLength);

	Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
	Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

	if (pd1.x > pd0.x)
	{
		Dx = -Dx;
	}
	if (pd1.y > pd0.y)
	{
		Dy = -Dy;
	}
    newPoint = new Point(pd1.x + Dx, pd1.y + Dy);

    //1 треугольник

   	intersections = circle_intersections(newPoint.x, newPoint.y, b1.length, pd1.x, pd1.y, a1.length);

   	if (!intersections || a1.length + b1.length <= +newLength)
   	{
   		alert('Недопустимая длина');
   		delete cansel_array[count_cansel];
   		count_cansel--;
   		return;
   	}

   	oldObshayaPoint1 = obshaya_point(a1, b1).clone();
    var len_0 = Path.Line(oldObshayaPoint1, intersections[0]);
    var len_1 = Path.Line(oldObshayaPoint1, intersections[1]);
   	if (len_0.length < len_1.length)
   	{
   		newObshayaPoint1 = intersections[0];
   	}
   	else
   	{
   		newObshayaPoint1 = intersections[1];
   	}
   	len_0.remove();
   	len_1.remove();

	//2 треугольник

   	intersections = circle_intersections(newPoint.x, newPoint.y, b2.length, pd1.x, pd1.y, a2.length);

   	if (!intersections || a2.length + b2.length <= +newLength)
   	{
   		alert('Недопустимая длина');
   		delete cansel_array[count_cansel];
   		count_cansel--;
   		return;
   	}

   	oldObshayaPoint2 = obshaya_point(a2, b2).clone();
    var len_0 = Path.Line(oldObshayaPoint2, intersections[0]);
    var len_1 = Path.Line(oldObshayaPoint2, intersections[1]);
   	if (len_0.length < len_1.length)
   	{
   		newObshayaPoint2 = intersections[0];
   	}
   	else
   	{
   		newObshayaPoint2 = intersections[1];
   	}
   	len_0.remove();
   	len_1.remove();


    points = [pd1.clone(), oldObshayaPoint1.clone()];
    cansel_array[count_cansel].line_id[a1.id] = points;
    points = [oldObshayaPoint1.clone(), pd0.clone()];
    cansel_array[count_cansel].line_id[b1.id] = points;

    a1.removeSegments();
    a1.addSegments([pd1, newObshayaPoint1]);
    b1.removeSegments();
    b1.addSegments([newObshayaPoint1, newPoint]);
    add_text_v_or_h(a1.id);
    add_text_v_or_h(b1.id);

    can_ver = moveVertexName(a1, b1, newObshayaPoint1);
    cansel_array[count_cansel].vert[0] = can_ver;
	cansel_array[count_cansel].vert[1] = new Point(oldObshayaPoint1.x - 10, oldObshayaPoint1.y - 5);


	points = [pd1.clone(), oldObshayaPoint2.clone()];
    cansel_array[count_cansel].line_id[a2.id] = points;
    points = [oldObshayaPoint2.clone(), pd0.clone()];
    cansel_array[count_cansel].line_id[b2.id] = points;

    a2.removeSegments();
    a2.addSegments([pd1, newObshayaPoint2]);
    b2.removeSegments();
    b2.addSegments([newObshayaPoint2, newPoint]);
    add_text_v_or_h(a2.id);
    add_text_v_or_h(b2.id);

    can_ver = moveVertexName(a2, b2, newObshayaPoint2);
    cansel_array[count_cansel].vert[2] = can_ver;
	cansel_array[count_cansel].vert[3] = new Point(oldObshayaPoint2.x - 10, oldObshayaPoint2.y - 5);


	points = [pd0.clone(), pd1.clone()];
    cansel_array[count_cansel].diag[index] = points;

	c.removeSegments();
    c.addSegments([newPoint, pd1]);
    add_text_diag(index);
    can_ver = moveVertexName(b1, b2, newPoint);
    cansel_array[count_cansel].vert[4] = can_ver;
	cansel_array[count_cansel].vert[5] = new Point(pd0.x - 10, pd0.y - 5);
   	fixed_diags[diag_sort[index].id] = true;
}

function moveVertexName(line1, line2, newPoint)
{
	var pt_id;
	for (var key in text_points_lines_id)
	{
		if (+text_points_lines_id[key].id_line1 === line1.id && +text_points_lines_id[key].id_line2 === line2.id
			|| +text_points_lines_id[key].id_line2 === line1.id && +text_points_lines_id[key].id_line1 === line2.id)
		{
			pt_id = text_points_lines_id[key].id_pt;
			break;
		}
	}

	for (var key = text_points.length; key--;)
	{
		//console.log(text_points[key].id, 'mov_ver_textid');
		if (text_points[key].id === +pt_id)
		{
	    	text_points[key].point = new Point(newPoint.x - 10, newPoint.y - 5);
	    	//console.log(key, 'mov_ver_return');
	    	return key;
		}
	}
}

var ok_enter_process, triangulate_bool = false;

function ok_enter_all()
{
	var kol_fix = 0;
	for (var key in fixed_walls)
	{
		if (fixed_walls[key])
		{
			kol_fix++;
		}
	}
	if (kol_fix === lines_sort.length - 1 && !triangulate_bool)
	{
		ok_enter_process = ok_enter.process();
		elem_preloader.style.display = 'block';
		setTimeout(ok_enter_process, 200);
	}
	else
	{
		ok_enter();
	}

}

var ok_enter = function()
{
	if (ready)
	{
		return;
	}
	var str_length = elem_newLength.value;
	var regexp = /^\d+$/;
	ready = false;
	var k, l, l1;
	if (regexp.test(str_length))
	{
		if (str_length < 3)
		{
			elem_preloader.style.display = 'none';
			alert('Слишком маленькая длина!');
			return;
		}
		if (str_length > 10000)
		{
			elem_preloader.style.display = 'none';
			alert('Слишком большая длина!');
			return;
		}
		clearInterval(timer_mig);
		if (triangulate_bool)
		{
			for (var key = 0; key < diag_sort.length; key++)
			{
				if (!fixed_diags[diag_sort[key].id])
		    	{
					k = elem_jform_n9.value;
		    		if (+k !== 4)
		    		{
			    		change_length_diag(key, str_length, false);
			    		sdvig();
	    				zoom(1);
			    	}
			    	else
			    	{
			    		change_length_diag_4angle(key, str_length, false);
			    		sdvig();
	    				zoom(1);
			    	}
		    		break;
		    	}
			}
			for (var key = 0; key < diag_sort.length; key++)
		    {
		    	if (fixed_diags[diag_sort[key].id])
		    	{
		    		diag_sort[key].strokeColor = 'green';
		    	}
		    	else
		    	{
		    		elem_preloader.style.display = 'none';
		    		diag_sort[key].strokeColor = 'red';
		    		text_diag[key].fillColor = 'Maroon';
		    		timer_mig = setInterval(migalka, 500, diag_sort[key]);
		    		elem_newLength.focus();
		    		elem_newLength.value = Math.round(diag_sort[key].length);
		    		elem_newLength.select();
		    		first_click = false;
		    		return;
		    	}
		    }
		    square();
		    elem_window.style.display = 'none';
		    text_points_sdvig();

		    ready = true;
		}
		else
		{
			for (var i = lines_sort.length; i--;)
			{
				lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
				lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
				lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
				lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
			}
			okruglenie_all_segments();
			for (var key = 0; key < lines_sort.length; key++)
		    {
		    	if (!fixed_walls[lines_sort[key].id])
		    	{
		    		count_cansel++;
					cansel_array[count_cansel] = {line_id: [], vert: [], fixed_id: lines_sort[key].id, razv_walls: [], vid: '2'};
		    		change_length(lines_sort[key], str_length, key);
		    		sdvig();
	    			zoom(1);
		    		break;
		    	}
		    }
		    for (var key = 0; key < lines_sort.length; key++)
		    {
		    	if (fixed_walls[lines_sort[key].id])
		    	{
		    		lines_sort[key].strokeColor = 'green';
		    	}
		    	else
		    	{
		    		elem_preloader.style.display = 'none';
		    		lines_sort[key].strokeColor = 'red';
		    		text_lines[lines_sort[key].id].fillColor = 'Maroon';
		    		timer_mig = setInterval(migalka, 500, lines_sort[key]);
		    		elem_newLength.focus();
		    		elem_newLength.value = Math.round(lines_sort[key].length);
		    		elem_newLength.select();
		    		first_click = false;
		    		return;
		    	}
		    }
		    k = 0;
		    for (var key = g_points.length; key--;)
		    {
		    	k++;
		    }
		    elem_jform_n9.value = k;
			perimetr();
			l = 0;
			for (var key in fixed_walls)
			{
				l++;
			}
			l1 = 0;
			for (var key in lines)
			{
				l1++;
			}

			if(l1 === l)
			{
				for (var key = lines_sort.length; key--;)
				{
					lines_sort[key].segments[0].point.x = +lines_sort[key].segments[0].point.x.toFixed(2);
					lines_sort[key].segments[0].point.y = +lines_sort[key].segments[0].point.y.toFixed(2);
					lines_sort[key].segments[1].point.x = +lines_sort[key].segments[1].point.x.toFixed(2);
					lines_sort[key].segments[1].point.y = +lines_sort[key].segments[1].point.y.toFixed(2);
				}
				triangulator();
				if (triangles_count() !== k - 2)
				{
					for (var key = 3; key--;)
					{
						pulemet();
						//console.log('pulemet');
						if (triangles_count() === k - 2)
						{
							break;
						}
					}
				}
				if (triangles_count() !== k - 2)
				{
					alert('Ошибка в построении диагоналей! Площадь будет посчитана неверно!');
					//console.log(diag);
					//console.log(diag_sort);
					//console.log(triangles_count());
				}
			}
			else
			{
				elem_preloader.style.display = 'none';
				alert('Ошибка! Перестойте чертеж заново.');
				return;
			}
		    diag_sortirovka();

		    for (var i = lines_sort.length; i--;)
			{
				lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
				lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
				lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
				lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
			}
			for (var i = diag_sort.length; i--;)
			{
				diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
				diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
				diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
				diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
			}
			okruglenie_all_segments();

	    	for (var i = 0; i < diag_sort.length; i++)
			{
				add_text_diag(i);
			}
		    if (lines_sort.length > 3)
		    {
			    triangulate_bool = true;
			    diag_sort[0].strokeColor = 'red';
			    text_diag[0].fillColor = 'Maroon';
			    timer_mig = setInterval(migalka, 500, diag_sort[0]);
	    		elem_newLength.focus();
	    		elem_newLength.value = Math.round(diag_sort[0].length);
	    		elem_newLength.select();
	    		first_click = false;
	    	}
	    	else
	    	{
	    		var a = 0, b = 0, c = 0, p = 0, s = 0;
				a = lines_sort[0].length;
				b = lines_sort[1].length;
				c = lines_sort[2].length;
				p = (a + b + c) / 2;
				s = Math.sqrt(p*(p-a)*(p-b)*(p-c));
				s = s / 10000;
				elem_jform_n4.value = s.toFixed(2);
				elem_window.style.display = 'none';
				text_points_sdvig();

				ready = true;
	    	}
		}	
	}
	else
	{
		alert('Недопустимые символы!');
	}
	elem_preloader.style.display = 'none';
};

function diag_sortirovka()
{
	var add_bool = true;
	var hitResults1, hitResults0;
	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 4)
		{
			diag_sort.push(diag[i]);
		}
	}
	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 3)
		{
			diag_sort.push(diag[i]);
		}
	}
	for (var i in diag)
	{
		hitResults0 = project.hitTestAll(diag[i].segments[0].point, {class: Path, segments: true, tolerance: 2});
		hitResults1 = project.hitTestAll(diag[i].segments[1].point, {class: Path, segments: true, tolerance: 2});
		if (diag_count_walls(diag[i]) === 2 && diag_count_fixed_diags(diag[i]) >= 2 && (hitResults0.length === 3 || hitResults1.length === 3))
		{
			add_bool = true;
			for (var j = 0; j < diag_sort.length; j++)
			{
				if (diag_sort[j].id === diag[i].id)
				{
					add_bool = false;
				}
			}
			if (add_bool)
			{
				diag_sort.push(diag[i]);
			}
		}
	}

	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 2 && diag_count_fixed_diags(diag[i]) >= 2)
		{
			add_bool = true;
			for (var j = 0; j < diag_sort.length; j++)
			{
				if (diag_sort[j].id === diag[i].id)
				{
					add_bool = false;
				}
			}
			if (add_bool)
			{
				diag_sort.push(diag[i]);
			}
		}
	}
	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 2 && diag_count_fixed_diags(diag[i]) >= 1)
		{
			add_bool = true;
			for (var j = 0; j < diag_sort.length; j++)
			{
				if (diag_sort[j].id === diag[i].id)
				{
					add_bool = false;
				}
			}
			if (add_bool)
			{
				diag_sort.push(diag[i]);
			}
		}
	}
	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 2 && diag_count_fixed_diags(diag[i]) >= 0)
		{
			add_bool = true;
			for (var j = 0; j < diag_sort.length; j++)
			{
				if (diag_sort[j].id === diag[i].id)
				{
					add_bool = false;
				}
			}
			if (add_bool)
			{
				diag_sort.push(diag[i]);
			}
		}
	}

	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 1)
		{
			diag_sort.push(diag[i]);
		}
	}
	for (var i in diag)
	{
		if (diag_count_walls(diag[i]) === 0)
		{
			diag_sort.push(diag[i]);
		}
	}
}

function diag_count_walls(diag)
{
	var count = 0, op;
	var hitResults0 = project.hitTestAll(diag.segments[0].point, {class: Path, segments: true, tolerance: 2});
	var hitResults1 = project.hitTestAll(diag.segments[1].point, {class: Path, segments: true, tolerance: 2});
	for (var key0 = hitResults0.length; key0--;)
	{
		if (lines_ravny(hitResults0[key0].item, diag) || hitResults0[key0].item.segments.length !== 2)
		{
			continue;
		}
		for (var key1 = hitResults1.length; key1--;)
		{
			if (!lines_ravny(hitResults1[key1].item, diag) && hitResults1[key1].item.segments.length === 2)
			{
				op = obshaya_point(hitResults0[key0].item, hitResults1[key1].item);
				if (op !== null)
				{
					if (hitResults0[key0].item.strokeWidth === 3)
					{
						count++;
					}
					if (hitResults1[key1].item.strokeWidth === 3)
					{
						count++;
					}
				}
			}
		}
	}
	return count;
}

function diag_count_fixed_diags(diag)
{
	var count = 0, op;
	var hitResults0 = project.hitTestAll(diag.segments[0].point, {class: Path, segments: true, tolerance: 2});
	var hitResults1 = project.hitTestAll(diag.segments[1].point, {class: Path, segments: true, tolerance: 2});
	for (var key0 = hitResults0.length; key0--;)
	{
		if (lines_ravny(hitResults0[key0].item, diag) || hitResults0[key0].item.segments.length !== 2)
		{
			continue;
		}
		for (var key1 = hitResults1.length; key1--;)
		{
			if (!lines_ravny(hitResults1[key1].item, diag) && hitResults1[key1].item.segments.length === 2)
			{
				op = obshaya_point(hitResults0[key0].item, hitResults1[key1].item);
				if (op !== null)
				{
					for (var j = 0; j < diag_sort.length; j++)
					{
						if (hitResults0[key0].item.id === diag_sort[j].id)
						{
							count++;
						}
						if (hitResults1[key1].item.id === diag_sort[j].id)
						{
							count++;
						}
					}
					
				}
			}
		}
	}
	return count;
}

function change_length_diag(index, length, rek)
{
	var hitResults0 = project.hitTestAll(diag_sort[index].segments[0].point, {class: Path, segments: true, tolerance: 2});
	var hitResults1 = project.hitTestAll(diag_sort[index].segments[1].point, {class: Path, segments: true, tolerance: 2});
	var Dx, Dy, coef;
	var pd0, pd1, pd2;
	var a1,b1,a2,b2,c,o,intersections,can_ver, op;
	var line_arr = [];
	var newPoint,newObshayaPoint1,newObshayaPoint2,oldObshayaPoint1,oldObshayaPoint2;
	var angle_new1, angle_old1, angle_rotate1, angle_new2, angle_old2, angle_rotate2, 
	angle_new3, angle_old3, angle_rotate3, angle_new4, angle_old4, angle_rotate4, angle_new, angle_old, angle_rotate;

	if (diag_count_walls(diag_sort[index]) === 3)
	{
		count_cansel++;
		cansel_array[count_cansel] = {line_id: [], diag: [], fixed_id: diag_sort[index].id, vid: '3_mega'};

		c = diag_sort[index];

		hitResults0 = project.hitTestAll(diag_sort[index].segments[0].point, {class: Path, segments: true, tolerance: 2});
		if (hitResults0.length === 3)
		{
			pd0 = diag_sort[index].segments[0].point.clone();
			pd1 = diag_sort[index].segments[1].point.clone();
		}
		else
		{
			pd0 = diag_sort[index].segments[1].point.clone();
			pd1 = diag_sort[index].segments[0].point.clone();
		}

		for (var i in lines)
		{
			for (var j in lines)
			{
				if (obshaya_point(lines[i], lines[j]) !== null && lines[i] !== lines[j])
				{
					if (!point_ravny(obshaya_point(lines[i], lines[j]), pd0) && !point_ravny(obshaya_point(lines[i], lines[j]), pd1))
					{
						if ((point_ravny(lines[i].segments[0].point, pd0) || point_ravny(lines[i].segments[1].point, pd0) 
							|| point_ravny(lines[i].segments[0].point, pd1) || point_ravny(lines[i].segments[1].point, pd1))
							&& (point_ravny(lines[j].segments[0].point, pd0) || point_ravny(lines[j].segments[1].point, pd0) 
							|| point_ravny(lines[j].segments[0].point, pd1) || point_ravny(lines[j].segments[1].point, pd1)))
						{
							a1 = lines[i];
							b1 = lines[j];
						}
					}
				}
			}
		}

		if (point_ravny(a1.segments[0].point, pd0) || point_ravny(a1.segments[1].point, pd0))
		{
			o = a1;
			a1 = b1;
			b1 = o;
		}

		for (var key in lines)
		{
			if (lines[key] !== a1 && lines[key] !== b1 && obshaya_point(lines[key], b1) !== null)
			{
				b2 = lines[key];
			}
		}

		for (var key = 0; key < diag_sort.length; key++)
		{
			if (diag_sort[key] !== c && obshaya_point(diag_sort[key], a1) !== null && obshaya_point(diag_sort[key], b2) !== null)
			{
				a2 = diag_sort[key];
				pd2 = obshaya_point(diag_sort[key], b2);
			}
		}

		var chis = new Decimal(new Decimal(c.segments[1].point.y).minus(c.segments[0].point.y)), 
		znam = new Decimal(new Decimal(c.segments[1].point.x).minus(c.segments[0].point.x));
		coef = chis.dividedBy(znam);

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
		Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

		if (pd1.x > pd0.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (pd1.y > pd0.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}
	    newPoint = new Point(new Decimal(pd1.x).plus(Dx).toNumber(), new Decimal(pd1.y).plus(Dy).toNumber());

	    //1 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b1.length, pd1.x, pd1.y, a1.length);

	   	if (!intersections || a1.length + b1.length <= +length)
	   	{
	   		alert('Недопустимая длина');
	   		delete cansel_array[count_cansel];
	   		count_cansel--;
	   		return;
	   	}

	   	oldObshayaPoint1 = obshaya_point(a1, b1).clone();
	    var len_0 = Path.Line(oldObshayaPoint1, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint1, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint1 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint1 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();


	   	intersections = circle_intersections(pd2.x, pd2.y, b2.length, pd1.x, pd1.y, +length);

	   	if (!intersections)
	   	{
	   		alert('Недопустимая длина');
	   		delete cansel_array[count_cansel];
	   		count_cansel--;
	   		return;
	   	}

	   	oldObshayaPoint2 = obshaya_point(b1, b2).clone();
	   	var len_0 = Path.Line(oldObshayaPoint2, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint2, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint2 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint2 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

	    angle_old = get_angle(newPoint, pd1);
	    angle_new = get_angle(newObshayaPoint2, pd1);
	    angle_rotate = new Decimal(angle_old).minus(angle_new).toNumber();


		for (var key = 0; key < lines_sort.length; key++)
	    {
	    	points = [lines_sort[key].segments[0].point.clone(), lines_sort[key].segments[1].point.clone()];
	    	cansel_array[count_cansel].line_id[lines_sort[key].id] = points;
	    }

	    for (var key = 0; key < diag_sort.length; key++)
	    {
	    	points = [diag_sort[key].segments[0].point.clone(), diag_sort[key].segments[1].point.clone()];
	    	cansel_array[count_cansel].diag[key] = points;
	    }

	    a1.removeSegments();
	    a1.addSegments([pd1, newObshayaPoint1]);
	    b1.removeSegments();
	    b1.addSegments([newObshayaPoint1, newPoint]); 

    	c.removeSegments();
	    c.addSegments([newPoint, pd1]);

	    b2.removeSegments();
	    b2.addSegments([pd2, newObshayaPoint2]);

	    a1.rotate(-angle_rotate, pd1);
	    b1.rotate(-angle_rotate, pd1);
	    c.rotate(-angle_rotate, pd1);

	    add_text_v_or_h(a1.id);
	    add_text_v_or_h(b1.id);
	    add_text_v_or_h(b2.id);
	    add_text_diag(index);

	    can_ver = moveVertexName(b1, b2, obshaya_point(b1, b2));

    	can_ver = moveVertexName(a1, b1, obshaya_point(a1, b1));

		fixed_diags[diag_sort[index].id] = true;
	}
	else if (diag_count_walls(diag_sort[index]) === 2 && (hitResults0.length === 3 || hitResults1.length === 3))
	{
		if (!rek)
		{
			count_cansel++;
			cansel_array[count_cansel] = {line_id: [], diag: [], fixed_id: diag_sort[index].id, vid: '3_mega'};

	    	for (var key = 0; key < lines_sort.length; key++)
		    {
		    	points = [lines_sort[key].segments[0].point.clone(), lines_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].line_id[lines_sort[key].id] = points;
		    }

		    for (var key = 0; key < diag_sort.length; key++)
		    {
		    	points = [diag_sort[key].segments[0].point.clone(), diag_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].diag[key] = points;
		    }
		}

		c = diag_sort[index];

		line_arr = line_arr_gen(index);

		for (var i = line_arr.length; i--;)
		{
			for (var j = line_arr.length; j--;)
			{
				if (line_arr[i] !== line_arr[j] && obshaya_point(line_arr[i], line_arr[j]) !== null 
					&& line_arr[i].strokeWidth === 3 && line_arr[j].strokeWidth === 3)
				{
					if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), diag_sort[index].segments[0].point))
					{
						pd0 = diag_sort[index].segments[0].point.clone();
						pd1 = diag_sort[index].segments[1].point.clone();
					}
					else if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), diag_sort[index].segments[1].point))
					{
						pd0 = diag_sort[index].segments[1].point.clone();
						pd1 = diag_sort[index].segments[0].point.clone();
					}
				}
			}
		}

		var break_bool = false;
		for (var i = line_arr.length; i--;)
		{
			for (var j = line_arr.length; j--;)
			{
				if (line_arr[i] !== line_arr[j] && line_arr[i].strokeWidth === 3 && line_arr[j].strokeWidth === 3 
					&& obshaya_point(line_arr[i], line_arr[j]) !== null)
				{
					if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), pd0))
					{
						b1 = line_arr[j];
						b2 = line_arr[i];
						line_arr.splice(line_arr.indexOf(b1),1);
						line_arr.splice(line_arr.indexOf(b2),1);
						break_bool = true;
						break;
					}
				}
			}
			if (break_bool)
			{
				break;
			}
		}

		for (var i = line_arr.length; i--;)
		{
			if (obshaya_point(line_arr[i], b1) !== null)
			{
				a1 = line_arr[i];
			}
			if (obshaya_point(line_arr[i], b2) !== null)
			{
				a2 = line_arr[i];
			}
		}

		var chis = new Decimal(new Decimal(c.segments[1].point.y).minus(c.segments[0].point.y)), 
		znam = new Decimal(new Decimal(c.segments[1].point.x).minus(c.segments[0].point.x));
		coef = chis.dividedBy(znam);

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
		Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();


		if (pd1.x > pd0.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (pd1.y > pd0.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}
	    newPoint = new Point(new Decimal(pd1.x).plus(Dx).toNumber(), new Decimal(pd1.y).plus(Dy).toNumber());

	    //1 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b1.length, pd1.x, pd1.y, a1.length);

	   	oldObshayaPoint1 = obshaya_point(a1, b1).clone();

	   	if (!intersections || a1.length + b1.length <= +length)
	   	{
	   		if (!fixed_diags[a1.id])
	   		{
	   			var napr, dop_length;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint1, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint1, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint1 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint1 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

		//2 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b2.length, pd1.x, pd1.y, a2.length);

	   	oldObshayaPoint2 = obshaya_point(a2, b2).clone();

	   	if (!intersections || a2.length + b2.length <= +length)
	   	{
	   		if (!fixed_diags[a2.id])
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint2, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint2, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint2 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint2 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();


	    var diag_f;
	    var op;

	    if (point_ravny(a1.segments[0].point, pd1))
		{
			op = a1.segments[1].point.clone();
		}
		else
		{
			op = a1.segments[0].point.clone();
		}

    	diag_f = a1;

		angle_old = get_angle(op, pd1);

    	var part_chert = find_part_chert_on_diag(diag_f, index);
    	var mass_w = part_chert.mass_w;
	    var mass_d = part_chert.mass_d;


    	var diag_f2;
	    var op2;

	    if (point_ravny(a2.segments[0].point, pd1))
		{
			op2 = a2.segments[1].point.clone();
		}
		else
		{
			op2 = a2.segments[0].point.clone();
		}

    	diag_f2 = a2;
    	angle_old2 = get_angle(op2, pd1);


    	part_chert = find_part_chert_on_diag(diag_f2, index);
    	var mass_w2 = part_chert.mass_w;
	    var mass_d2 = part_chert.mass_d;


	    angle_new = get_angle(newObshayaPoint1, pd1);
	    angle_rotate = new Decimal(angle_old).minus(angle_new).toNumber();



    	angle_new2 = get_angle(newObshayaPoint2, pd1);
    	angle_rotate2 = new Decimal(angle_old2).minus(angle_new2).toNumber();


    	for (var key = mass_w.length; key--;)
    	{
	    	mass_w[key].rotate(-angle_rotate, pd1);
    	}
    	for (var key = mass_d.length; key--;)
    	{
	    	mass_d[key].rotate(-angle_rotate, pd1);
    	}


    	for (var key = mass_w2.length; key--;)
    	{
	    	mass_w2[key].rotate(-angle_rotate2, pd1);
    	}
    	for (var key = mass_d2.length; key--;)
    	{
	    	mass_d2[key].rotate(-angle_rotate2, pd1);
    	}

	    c.removeSegments();
	    c.addSegments([pd1, newPoint]);

	    a1.removeSegments();
	    a1.addSegments([pd1, newObshayaPoint1]);
	    b1.removeSegments();
	    b1.addSegments([newObshayaPoint1, newPoint]);

	    a2.removeSegments();
	    a2.addSegments([pd1, newObshayaPoint2]);
	    b2.removeSegments();
	    b2.addSegments([newObshayaPoint2, newPoint]);


	    for (var key = 0; key < lines_sort.length; key++)
	    {
	    	add_text_v_or_h(lines_sort[key].id);
	    }

	    for (var key = 0; key < diag_sort.length; key++)
	    {
	    	add_text_diag(key);
	    }
	    
	    var j = 0;
	    for (var i = 0; i < lines_sort.length; i++)
	    {
	    	if (i === 0)
	    	{
	    		j = lines_sort.length - 1;
	    	}
	    	else
	    	{
	    		j = i - 1;
	    	}
	    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
	    	{
	    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
	    	}
	    }
	}
	else if (diag_count_walls(diag_sort[index]) === 2)
	{
		if (!rek)
		{
			count_cansel++;
			cansel_array[count_cansel] = {line_id: [], diag: [], fixed_id: diag_sort[index].id, vid: '3_mega'};

			for (var key = 0; key < lines_sort.length; key++)
		    {
		    	points = [lines_sort[key].segments[0].point.clone(), lines_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].line_id[lines_sort[key].id] = points;
		    }

		    for (var key = 0; key < diag_sort.length; key++)
		    {
		    	points = [diag_sort[key].segments[0].point.clone(), diag_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].diag[key] = points;
		    }
		}

		c = diag_sort[index];

		line_arr = line_arr_gen(index);

		g_points = getPathsPoints(lines_sort);
		var min_point = new Point(findMinAndMaxCordinate_g().minX, findMinAndMaxCordinate_g().minY);
		var max_point = new Point(findMinAndMaxCordinate_g().maxX, findMinAndMaxCordinate_g().maxY);
		var center_line = Path.Line(min_point, max_point);
		var center_point = center_line.position.clone();
		center_line.remove();

		pd0 = diag_sort[index].segments[0].point.clone();
		pd1 = diag_sort[index].segments[1].point.clone();

		var rast0 = Math.sqrt(Math.pow(pd0.x - center_point.x, 2) + Math.pow(pd0.y - center_point.y, 2));
		var rast1 = Math.sqrt(Math.pow(pd1.x - center_point.x, 2) + Math.pow(pd1.y - center_point.y, 2));
		if (rast0 < rast1)
		{
			pd0 = diag_sort[index].segments[1].point.clone();
			pd1 = diag_sort[index].segments[0].point.clone();
		}

		var break_bool = false;
		for (var i = line_arr.length; i--;)
		{
			for (var j = line_arr.length; j--;)
			{
				if (line_arr[i] !== line_arr[j] && obshaya_point(line_arr[i], line_arr[j]) !== null)
				{
					if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), pd0))
					{
						if (line_arr[i].strokeWidth === 3)
						{
							b1 = line_arr[i];
							b2 = line_arr[j];
						}
						else
						{
							b1 = line_arr[j];
							b2 = line_arr[i];
						}
						line_arr.splice(line_arr.indexOf(b1),1);
						line_arr.splice(line_arr.indexOf(b2),1);
						break_bool = true;
						break;
					}
				}
			}
			if (break_bool)
			{
				break;
			}
		}

		for (var i = line_arr.length; i--;)
		{
			if (obshaya_point(line_arr[i], b1) !== null)
			{
				a1 = line_arr[i];
			}
			if (obshaya_point(line_arr[i], b2) !== null)
			{
				a2 = line_arr[i];
			}
		}
		

		var chis = new Decimal(new Decimal(c.segments[1].point.y).minus(c.segments[0].point.y)), 
		znam = new Decimal(new Decimal(c.segments[1].point.x).minus(c.segments[0].point.x));
		coef = chis.dividedBy(znam);

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
		Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

		if (pd1.x > pd0.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (pd1.y > pd0.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}
	    newPoint = new Point(new Decimal(pd1.x).plus(Dx).toNumber(), new Decimal(pd1.y).plus(Dy).toNumber());

	    //1 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b1.length, pd1.x, pd1.y, a1.length);

	   	oldObshayaPoint1 = obshaya_point(a1, b1).clone();

	   	if (!intersections || a1.length + b1.length <= +length)
	   	{
	   		if (!fixed_diags[a1.id] && a1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else if (!fixed_diags[b1.id] && b1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + a1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - a1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === b1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint1, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint1, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint1 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint1 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();


		//2 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b2.length, pd1.x, pd1.y, a2.length);

	   	oldObshayaPoint2 = obshaya_point(a2, b2).clone();

	   	if (!intersections || a2.length + b2.length <= +length)
	   	{
	   		if (!fixed_diags[a2.id] && a2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else if (!fixed_diags[b2.id] && b2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + a2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - a2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === b2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint2, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint2, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint2 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint2 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();


	    var diag_f;
	    var op;

	    if (a1.strokeWidth === 1)
	    {
		    if (point_ravny(a1.segments[0].point, pd1))
			{
				op = a1.segments[1].point.clone();
			}
			else
			{
				op = a1.segments[0].point.clone();
			}

	    	diag_f = a1;
		}
		else
		{
			if (point_ravny(a2.segments[0].point, pd1))
			{
				op = a2.segments[1].point.clone();
			}
			else
			{
				op = a2.segments[0].point.clone();
			}

	    	diag_f = a2;
		}

		angle_old = get_angle(op, pd1);

    	var part_chert = find_part_chert_on_diag(diag_f, index);
    	var mass_w = part_chert.mass_w;
	    var mass_d = part_chert.mass_d;


    	var diag_f2;
	    var op2;

	    if (point_ravny(a2.segments[0].point, pd1))
		{
			op2 = a2.segments[1].point.clone();
		}
		else
		{
			op2 = a2.segments[0].point.clone();
		}

    	diag_f2 = b2;
    	angle_old2 = get_angle(op2, pd0);


    	part_chert = find_part_chert_on_diag(diag_f2, index);
    	var mass_w2 = part_chert.mass_w;
	    var mass_d2 = part_chert.mass_d;


    	if (a1.strokeWidth === 1)
	    {
	    	angle_new = get_angle(newObshayaPoint1, pd1);
	    }
	    else
	    {
	    	angle_new = get_angle(newObshayaPoint2, pd1);
	    }

	    angle_rotate = new Decimal(angle_old).minus(angle_new).toNumber();



    	angle_new2 = get_angle(newObshayaPoint2, newPoint);
    	angle_rotate2 = new Decimal(angle_old2).minus(angle_new2).toNumber();


    	for (var key = mass_w.length; key--;)
    	{
	    	mass_w[key].rotate(-angle_rotate, pd1);
    	}
    	for (var key = mass_d.length; key--;)
    	{
	    	mass_d[key].rotate(-angle_rotate, pd1);
    	}


    	var rast_sdvig_x = new Decimal(newPoint.x).minus(pd0.x).toNumber();
    	var rast_sdvig_y = new Decimal(newPoint.y).minus(pd0.y).toNumber();

    	for (var key = mass_w2.length; key--;)
    	{
    		mass_w2[key].position.x = new Decimal(mass_w2[key].position.x).plus(rast_sdvig_x);
    		mass_w2[key].position.y = new Decimal(mass_w2[key].position.y).plus(rast_sdvig_y);
	    	mass_w2[key].rotate(-angle_rotate2, newPoint);
    	}
    	for (var key = mass_d2.length; key--;)
    	{
    		mass_d2[key].position.x = new Decimal(mass_d2[key].position.x).plus(rast_sdvig_x);
    		mass_d2[key].position.y = new Decimal(mass_d2[key].position.y).plus(rast_sdvig_y);
	    	mass_d2[key].rotate(-angle_rotate2, newPoint);
    	}

	    c.removeSegments();
	    c.addSegments([pd1, newPoint]);

	    a1.removeSegments();
	    a1.addSegments([pd1, newObshayaPoint1]);
	    b1.removeSegments();
	    b1.addSegments([newObshayaPoint1, newPoint]);

	    a2.removeSegments();
	    a2.addSegments([pd1, newObshayaPoint2]);
	    b2.removeSegments();
	    b2.addSegments([newObshayaPoint2, newPoint]);


	    for (var key = 0; key < lines_sort.length; key++)
	    {
	    	add_text_v_or_h(lines_sort[key].id);
	    }

	    for (var key = 0; key < diag_sort.length; key++)
	    {
	    	add_text_diag(key);
	    }
	    
	    var j = 0;
	    for (var i = 0; i < lines_sort.length; i++)
	    {
	    	if (i === 0)
	    	{
	    		j = lines_sort.length - 1;
	    	}
	    	else
	    	{
	    		j = i - 1;
	    	}
	    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
	    	{
	    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
	    	}
	    }
	}
	else if (diag_count_walls(diag_sort[index]) === 1)
	{
		if (!rek)
		{
			count_cansel++;
			cansel_array[count_cansel] = {line_id: [], diag: [], fixed_id: diag_sort[index].id, vid: '3_mega'};

			for (var key = 0; key < lines_sort.length; key++)
		    {
		    	points = [lines_sort[key].segments[0].point.clone(), lines_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].line_id[lines_sort[key].id] = points;
		    }

		    for (var key = 0; key < diag_sort.length; key++)
		    {
		    	points = [diag_sort[key].segments[0].point.clone(), diag_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].diag[key] = points;
		    }
		}

		c = diag_sort[index];

		line_arr = line_arr_gen(index);

		for (var i = line_arr.length; i--;)
		{
			if (line_arr[i].strokeWidth === 3)
			{
				if (point_ravny(obshaya_point(line_arr[i], c), c.segments[0].point))
				{
					pd0 = c.segments[0].point.clone();
					pd1 = c.segments[1].point.clone();
				}
				else if (point_ravny(obshaya_point(line_arr[i], c), c.segments[1].point))
				{
					pd0 = c.segments[1].point.clone();
					pd1 = c.segments[0].point.clone();
				}
				break;
			}
		}

		var break_bool = false;
		for (var i = line_arr.length; i--;)
		{
			for (var j = line_arr.length; j--;)
			{
				if (line_arr[i] !== line_arr[j] && obshaya_point(line_arr[i], line_arr[j]) !== null)
				{
					if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), pd0))
					{
						if (line_arr[i].strokeWidth === 3)
						{
							b1 = line_arr[i];
							b2 = line_arr[j];
						}
						else
						{
							b1 = line_arr[j];
							b2 = line_arr[i];
						}
						line_arr.splice(line_arr.indexOf(b1),1);
						line_arr.splice(line_arr.indexOf(b2),1);
						break_bool = true;
						break;
					}
				}
			}
			if (break_bool)
			{
				break;
			}
		}

		for (var i = line_arr.length; i--;)
		{
			if (obshaya_point(line_arr[i], b1) !== null)
			{
				a1 = line_arr[i];
			}
			if (obshaya_point(line_arr[i], b2) !== null)
			{
				a2 = line_arr[i];
			}
		}
		

		var chis = new Decimal(new Decimal(c.segments[1].point.y).minus(c.segments[0].point.y)), 
		znam = new Decimal(new Decimal(c.segments[1].point.x).minus(c.segments[0].point.x));
		coef = chis.dividedBy(znam);

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
		Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

		if (pd1.x > pd0.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (pd1.y > pd0.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}
	    newPoint = new Point(new Decimal(pd1.x).plus(Dx).toNumber(), new Decimal(pd1.y).plus(Dy).toNumber());

	    //1 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b1.length, pd1.x, pd1.y, a1.length);

	   	oldObshayaPoint1 = obshaya_point(a1, b1).clone();

	   	if (!intersections || a1.length + b1.length <= +length)
	   	{
	   		if (!fixed_diags[a1.id] && a1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else if (!fixed_diags[b1.id] && b1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + a1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - a1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === b1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint1, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint1, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint1 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint1 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

		//2 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b2.length, pd1.x, pd1.y, a2.length);

	   	oldObshayaPoint2 = obshaya_point(a2, b2).clone();

	   	if (!intersections || a2.length + b2.length <= +length)
	   	{
	   		if (!fixed_diags[a2.id] && a2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else if (!fixed_diags[b2.id] && b2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + a2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - a2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === b2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	   	var len_0 = Path.Line(oldObshayaPoint2, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint2, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint2 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint2 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

	    var diag_f1;
	    var op1;

	    if (point_ravny(a1.segments[0].point, pd1))
		{
			op1 = a1.segments[1].point.clone();
		}
		else
		{
			op1 = a1.segments[0].point.clone();
		}

    	diag_f1 = a1;
    	angle_old1 = get_angle(op1, pd1);


    	var part_chert = find_part_chert_on_diag(diag_f1, index);
    	var mass_w1 = part_chert.mass_w;
	    var mass_d1 = part_chert.mass_d;

	    angle_new1 = get_angle(newObshayaPoint1, pd1);

	    angle_rotate1 = new Decimal(angle_old1).minus(angle_new1).toNumber();


	    var diag_f2;
	    var op2;

	    if (point_ravny(a2.segments[0].point, pd1))
		{
			op2 = a2.segments[1].point.clone();
		}
		else
		{
			op2 = a2.segments[0].point.clone();
		}

    	diag_f2 = a2;
    	angle_old2 = get_angle(op2, pd1);


    	var part_chert = find_part_chert_on_diag(diag_f2, index);
    	var mass_w2 = part_chert.mass_w;
	    var mass_d2 = part_chert.mass_d;

	    angle_new2 = get_angle(newObshayaPoint2, pd1);

	    angle_rotate2 = new Decimal(angle_old2).minus(angle_new2).toNumber();


	    var diag_f3;

    	diag_f3 = b2;
    	angle_old3 = get_angle(op2, pd0);


    	var part_chert = find_part_chert_on_diag(diag_f3, index);
    	var mass_w3 = part_chert.mass_w;
	    var mass_d3 = part_chert.mass_d;

	    angle_new3 = get_angle(newObshayaPoint2, newPoint);

	    angle_rotate3 = new Decimal(angle_old3).minus(angle_new3).toNumber();


    	for (var key = mass_w1.length; key--;)
    	{
	    	mass_w1[key].rotate(-angle_rotate1, pd1);
    	}
    	for (var key = mass_d1.length; key--;)
    	{
	    	mass_d1[key].rotate(-angle_rotate1, pd1);
    	}

    	for (var key = mass_w2.length; key--;)
    	{
	    	mass_w2[key].rotate(-angle_rotate2, pd1);
    	}
    	for (var key = mass_d2.length; key--;)
    	{
	    	mass_d2[key].rotate(-angle_rotate2, pd1);
    	}

    	var rast_sdvig_x = new Decimal(newPoint.x).minus(pd0.x).toNumber();
    	var rast_sdvig_y = new Decimal(newPoint.y).minus(pd0.y).toNumber();

    	for (var key = mass_w3.length; key--;)
    	{
    		mass_w3[key].position.x = new Decimal(mass_w3[key].position.x).plus(rast_sdvig_x);
    		mass_w3[key].position.y = new Decimal(mass_w3[key].position.y).plus(rast_sdvig_y);
	    	mass_w3[key].rotate(-angle_rotate3, newPoint);
    	}
    	for (var key = mass_d3.length; key--;)
    	{
    		mass_d3[key].position.x = new Decimal(mass_d3[key].position.x).plus(rast_sdvig_x);
    		mass_d3[key].position.y = new Decimal(mass_d3[key].position.y).plus(rast_sdvig_y);
	    	mass_d3[key].rotate(-angle_rotate3, newPoint);
    	}

	    c.removeSegments();
	    c.addSegments([pd1, newPoint]);

	    a1.removeSegments();
	    a1.addSegments([pd1, newObshayaPoint1]);
	    b1.removeSegments();
	    b1.addSegments([newObshayaPoint1, newPoint]);

	    a2.removeSegments();
	    a2.addSegments([pd1, newObshayaPoint2]);
	    b2.removeSegments();
	    b2.addSegments([newObshayaPoint2, newPoint]);


	    for (var key = 0; key < lines_sort.length; key++)
	    {
	    	add_text_v_or_h(lines_sort[key].id);
	    }

	    for (var key = 0; key < diag_sort.length; key++)
	    {
	    	add_text_diag(key);
	    }
	    
	    var j = 0;
	    for (var i = 0; i < lines_sort.length; i++)
	    {
	    	if (i === 0)
	    	{
	    		j = lines_sort.length - 1;
	    	}
	    	else
	    	{
	    		j = i - 1;
	    	}
	    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
	    	{
	    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
	    	}
	    }
	}
	else if (diag_count_walls(diag_sort[index]) === 0)
	{
		if (!rek)
		{
			count_cansel++;
			cansel_array[count_cansel] = {line_id: [], diag: [], fixed_id: diag_sort[index].id, vid: '3_mega'};

			for (var key = 0; key < lines_sort.length; key++)
		    {
		    	points = [lines_sort[key].segments[0].point.clone(), lines_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].line_id[lines_sort[key].id] = points;
		    }

		    for (var key = 0; key < diag_sort.length; key++)
		    {
		    	points = [diag_sort[key].segments[0].point.clone(), diag_sort[key].segments[1].point.clone()];
		    	cansel_array[count_cansel].diag[key] = points;
		    }
		}

		c = diag_sort[index];

		line_arr = line_arr_gen(index);

		g_points = getPathsPoints(lines_sort);
		var min_point = new Point(findMinAndMaxCordinate_g().minX, findMinAndMaxCordinate_g().minY);
		var max_point = new Point(findMinAndMaxCordinate_g().maxX, findMinAndMaxCordinate_g().maxY);
		var center_line = Path.Line(min_point, max_point);
		var center_point = center_line.position.clone();
		center_line.remove();

		pd0 = diag_sort[index].segments[0].point.clone();
		pd1 = diag_sort[index].segments[1].point.clone();

		var rast0 = Math.sqrt(Math.pow(pd0.x - center_point.x, 2) + Math.pow(pd0.y - center_point.y, 2));
		var rast1 = Math.sqrt(Math.pow(pd1.x - center_point.x, 2) + Math.pow(pd1.y - center_point.y, 2));
		if (rast0 < rast1)
		{
			pd0 = diag_sort[index].segments[1].point.clone();
			pd1 = diag_sort[index].segments[0].point.clone();
		}

		var break_bool = false;
		for (var i = line_arr.length; i--;)
		{
			for (var j = line_arr.length; j--;)
			{
				if (line_arr[i] !== line_arr[j] && obshaya_point(line_arr[i], line_arr[j]) !== null)
				{
					if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), pd0))
					{
						b1 = line_arr[i];
						b2 = line_arr[j];
						line_arr.splice(line_arr.indexOf(b1),1);
						line_arr.splice(line_arr.indexOf(b2),1);
						break_bool = true;
						break;
					}
				}
			}
			if (break_bool)
			{
				break;
			}
		}

		for (var i = line_arr.length; i--;)
		{
			if (obshaya_point(line_arr[i], b1) !== null)
			{
				a1 = line_arr[i];
			}
			if (obshaya_point(line_arr[i], b2) !== null)
			{
				a2 = line_arr[i];
			}
		}
		

		var chis = new Decimal(new Decimal(c.segments[1].point.y).minus(c.segments[0].point.y)), 
		znam = new Decimal(new Decimal(c.segments[1].point.x).minus(c.segments[0].point.x));
		coef = chis.dividedBy(znam);

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
		Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

		if (pd1.x > pd0.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (pd1.y > pd0.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}
	    newPoint = new Point(new Decimal(pd1.x).plus(Dx).toNumber(), new Decimal(pd1.y).plus(Dy).toNumber());

	    //1 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b1.length, pd1.x, pd1.y, a1.length);

	   	oldObshayaPoint1 = obshaya_point(a1, b1).clone();

	   	if (!intersections || a1.length + b1.length <= +length)
	   	{
	   		if (!fixed_diags[a1.id] && a1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else if (!fixed_diags[b1.id] && b1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + a1.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - a1.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === b1.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint1, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint1, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint1 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint1 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

		//2 треугольник

	   	intersections = circle_intersections(newPoint.x, newPoint.y, b2.length, pd1.x, pd1.y, a2.length);

	   	oldObshayaPoint2 = obshaya_point(a2, b2).clone();

	   	if (!intersections || a2.length + b2.length <= +length)
	   	{
	   		if (!fixed_diags[a2.id] && a2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + b2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - b2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === a2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else if (!fixed_diags[b2.id] && b2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < diag_sort[index].length && angle_dd < 90 || +length > diag_sort[index].length && angle_dd >= 90)
	   			{
	   				napr = '<';
	   				dop_length = +length + a2.length - 3;
	   			}
	   			else
	   			{
	   				napr = '>';
	   				dop_length = Math.abs(+length - a2.length) + 3;
	   			}

	   			for (var key = diag_sort.length; key--;)
   				{
   					if (diag_sort[key].id === b2.id)
   					{
   						stop_rek = false;
   						super_change_diag(index, +length, key, napr, dop_length, rek);
   						return;
   					}
   				}
	   		}
	   		else
	   		{
	   			stop_rek = true;
	   			if (!rek)
		   		{
		   			alert('Недопустимая длина');
		   		}
	   		}
	   		return;
	   	}

	    var len_0 = Path.Line(oldObshayaPoint2, intersections[0]);
	    var len_1 = Path.Line(oldObshayaPoint2, intersections[1]);
	   	if (len_0.length < len_1.length)
	   	{
	   		newObshayaPoint2 = intersections[0];
	   	}
	   	else
	   	{
	   		newObshayaPoint2 = intersections[1];
	   	}
	   	len_0.remove();
	   	len_1.remove();

	    var diag_f1;
	    var op1;

	    if (point_ravny(a1.segments[0].point, pd1))
		{
			op1 = a1.segments[1].point.clone();
		}
		else
		{
			op1 = a1.segments[0].point.clone();
		}

    	diag_f1 = a1;
    	angle_old1 = get_angle(op1, pd1);


    	var part_chert = find_part_chert_on_diag(diag_f1, index);
    	var mass_w1 = part_chert.mass_w;
	    var mass_d1 = part_chert.mass_d;

	    angle_new1 = get_angle(newObshayaPoint1, pd1);

	    angle_rotate1 = new Decimal(angle_old1).minus(angle_new1).toNumber();


	    var diag_f2;
	    var op2;

	    if (point_ravny(a2.segments[0].point, pd1))
		{
			op2 = a2.segments[1].point.clone();
		}
		else
		{
			op2 = a2.segments[0].point.clone();
		}

    	diag_f2 = a2;
    	angle_old2 = get_angle(op2, pd1);


    	var part_chert = find_part_chert_on_diag(diag_f2, index);
    	var mass_w2 = part_chert.mass_w;
	    var mass_d2 = part_chert.mass_d;

	    angle_new2 = get_angle(newObshayaPoint2, pd1);

	    angle_rotate2 = new Decimal(angle_old2).minus(angle_new2).toNumber();


	    var diag_f3;

    	diag_f3 = b2;
    	angle_old3 = get_angle(op2, pd0);


    	var part_chert = find_part_chert_on_diag(diag_f3, index);
    	var mass_w3 = part_chert.mass_w;
	    var mass_d3 = part_chert.mass_d;

	    angle_new3 = get_angle(newObshayaPoint2, newPoint);

	    angle_rotate3 = new Decimal(angle_old3).minus(angle_new3).toNumber();


	    var diag_f4;

    	diag_f4 = b1;
    	angle_old4 = get_angle(op1, pd0);


    	var part_chert = find_part_chert_on_diag(diag_f4, index);
    	var mass_w4 = part_chert.mass_w;
	    var mass_d4 = part_chert.mass_d;

	    angle_new4 = get_angle(newObshayaPoint1, newPoint);

	    angle_rotate4 = new Decimal(angle_old4).minus(angle_new4).toNumber();


    	for (var key = mass_w1.length; key--;)
    	{
	    	mass_w1[key].rotate(-angle_rotate1, pd1);
    	}
    	for (var key = mass_d1.length; key--;)
    	{
	    	mass_d1[key].rotate(-angle_rotate1, pd1);
    	}

    	for (var key = mass_w2.length; key--;)
    	{
	    	mass_w2[key].rotate(-angle_rotate2, pd1);
    	}
    	for (var key = mass_d2.length; key--;)
    	{
	    	mass_d2[key].rotate(-angle_rotate2, pd1);
    	}

    	var rast_sdvig_x = new Decimal(newPoint.x).minus(pd0.x).toNumber();
    	var rast_sdvig_y = new Decimal(newPoint.y).minus(pd0.y).toNumber();

    	for (var key = mass_w3.length; key--;)
    	{
    		mass_w3[key].position.x = new Decimal(mass_w3[key].position.x).plus(rast_sdvig_x);
    		mass_w3[key].position.y = new Decimal(mass_w3[key].position.y).plus(rast_sdvig_y);
	    	mass_w3[key].rotate(-angle_rotate3, newPoint);
    	}
    	for (var key = mass_d3.length; key--;)
    	{
    		mass_d3[key].position.x = new Decimal(mass_d3[key].position.x).plus(rast_sdvig_x);
    		mass_d3[key].position.y = new Decimal(mass_d3[key].position.y).plus(rast_sdvig_y);
	    	mass_d3[key].rotate(-angle_rotate3, newPoint);
    	}


    	for (var key = mass_w4.length; key--;)
    	{
    		mass_w4[key].position.x = new Decimal(mass_w4[key].position.x).plus(rast_sdvig_x);
    		mass_w4[key].position.y = new Decimal(mass_w4[key].position.y).plus(rast_sdvig_y);
	    	mass_w4[key].rotate(-angle_rotate4, newPoint);
    	}
    	for (var key = mass_d4.length; key--;)
    	{
    		mass_d4[key].position.x = new Decimal(mass_d4[key].position.x).plus(rast_sdvig_x);
    		mass_d4[key].position.y = new Decimal(mass_d4[key].position.y).plus(rast_sdvig_y);
	    	mass_d4[key].rotate(-angle_rotate4, newPoint);
    	}

	    c.removeSegments();
	    c.addSegments([pd1, newPoint]);

	    a1.removeSegments();
	    a1.addSegments([pd1, newObshayaPoint1]);
	    b1.removeSegments();
	    b1.addSegments([newObshayaPoint1, newPoint]);

	    a2.removeSegments();
	    a2.addSegments([pd1, newObshayaPoint2]);
	    b2.removeSegments();
	    b2.addSegments([newObshayaPoint2, newPoint]);


	    for (var key = 0; key < lines_sort.length; key++)
	    {
	    	add_text_v_or_h(lines_sort[key].id);
	    }

	    for (var key = 0; key < diag_sort.length; key++)
	    {
	    	add_text_diag(key);
	    }
	    
	    var j = 0;
	    for (var i = 0; i < lines_sort.length; i++)
	    {
	    	if (i === 0)
	    	{
	    		j = lines_sort.length - 1;
	    	}
	    	else
	    	{
	    		j = i - 1;
	    	}
	    	if (lines_sort[i] !== lines_sort[j] && obshaya_point(lines_sort[i], lines_sort[j]) !== null)
	    	{
	    		moveVertexName(lines_sort[i], lines_sort[j], obshaya_point(lines_sort[i], lines_sort[j]));
	    	}
	    }
	}

	if (!rek)
	{
	    fixed_diags[diag_sort[index].id] = true;
	}

	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
		lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
		lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
		lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
		diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
		diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
		diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();
}

var stop_rek = false;

function super_change_diag(main_diag_index, newLength, nefixed_diag_index, napr, dop_length, rek)
{
	//console.log('super_change_diag', newLength);
	dop_length = Math.round(dop_length);
	var ch_length = Math.round(diag_sort[nefixed_diag_index].length);
	while ((ch_length < dop_length - 2 || ch_length > dop_length + 2) && !stop_rek)
	{
		if (napr === '<')
		{
			ch_length -= 2;
		}
		else if (napr === '>')
		{
			ch_length += 2;
		}
		change_length_diag(nefixed_diag_index, ch_length, true);
	}
	if (stop_rek)
	{
		alert('Недопустимая длина!');
		cancelLastAction();
	}
	else
	{
		change_length_diag(main_diag_index, newLength, true);
		if (rek === false)
		{
			fixed_diags[diag_sort[main_diag_index].id] = true;
		}
	}
}


function circle_intersections(xp1_o, yp1_o, r1, xp2_o, yp2_o, r2)
{
	if (xp1_o === xp2_o && yp1_o === yp2_o)
	{
		return false;
	}
	var xp1 = 0, yp1 = 0;
	var xp2 = new Decimal(xp2_o).minus(xp1_o).toNumber();
	var yp2 = new Decimal(yp2_o).minus(yp1_o).toNumber();
	var c_chis_dec = Decimal.pow(r2, 2).minus(Decimal.pow(xp2, 2)).minus(Decimal.pow(yp2, 2)).minus(Decimal.pow(r1, 2));
	var c = c_chis_dec.dividedBy(-2).toNumber();

	if (xp2 !== 0)
	{
		var a = Decimal.pow(yp2, 2).plus(Decimal.pow(xp2, 2)).toNumber();
		var b = new Decimal(-2).times(yp2).times(c).toNumber();
		var e = Decimal.pow(c, 2).minus(Decimal.pow(r1, 2).times(Decimal.pow(xp2, 2))).toNumber();
		var d = Decimal.pow(b, 2).minus(new Decimal(4).times(a).times(e)).toNumber();
		if (d <= 0)
		{
			return false;
		}
		else
		{
			var y1 = new Decimal(new Decimal(-b).plus(Decimal.sqrt(d))).dividedBy(new Decimal(2).times(a)).toNumber();
			var y2 = new Decimal(new Decimal(-b).minus(Decimal.sqrt(d))).dividedBy(new Decimal(2).times(a)).toNumber();
			var x1 = new Decimal(new Decimal(c).minus(new Decimal(y1).times(yp2))).dividedBy(xp2).toNumber();
			var x2 = new Decimal(new Decimal(c).minus(new Decimal(y2).times(yp2))).dividedBy(xp2).toNumber();
		}
	}
	else
	{
		var y1 = new Decimal(c).dividedBy(yp2).toNumber();
		var y2 = y1;
		var x1 = Decimal.sqrt(Decimal.pow(r1, 2).minus(Decimal.pow(c, 2).dividedBy(Decimal.pow(yp2, 2)))).toNumber();
		var x2 = -Decimal.sqrt(Decimal.pow(r1, 2).minus(Decimal.pow(c, 2).dividedBy(Decimal.pow(yp2, 2)))).toNumber();
	}
	x1 = +(new Decimal(x1).plus(xp1_o).toNumber()).toFixed(2);
	x2 = +(new Decimal(x2).plus(xp1_o).toNumber()).toFixed(2);
	y1 = +(new Decimal(y1).plus(yp1_o).toNumber()).toFixed(2);
	y2 = +(new Decimal(y2).plus(yp1_o).toNumber()).toFixed(2);

	return [new Point(x1, y1), new Point(x2, y2)];
}

function line_arr_gen(index)
{
	var line_arr = [];
	var hitResults0 = project.hitTestAll(diag_sort[index].segments[0].point, {class: Path, segments: true, tolerance: 2});
	var hitResults1 = project.hitTestAll(diag_sort[index].segments[1].point, {class: Path, segments: true, tolerance: 2});
	var op;
	for (var key0 = hitResults0.length; key0--;)
	{
		if (lines_ravny(hitResults0[key0].item, diag_sort[index]) || hitResults0[key0].item.segments.length !== 2)
		{
			continue;
		}
		for (var key1 = hitResults1.length; key1--;)
		{
			if (!lines_ravny(hitResults1[key1].item, diag_sort[index]) && hitResults1[key1].item.segments.length === 2)
			{
				op = obshaya_point(hitResults0[key0].item, hitResults1[key1].item);
				if (op !== null)
				{
					line_arr.push(hitResults0[key0].item);
					line_arr.push(hitResults1[key1].item);
				}
			}
		}
	}
	return line_arr;
}

function okruglenie_all_segments()
{
	var j = 0, min_rast, p1, p2;
	for (var i = lines_sort.length; i--;)
	{
		if (i === 0)
		{
			j = lines_sort.length - 1;
		}
		else
		{
			j = i - 1;
		}
		min_rast = 40000;
		for (var ii = lines_sort[i].segments.length; ii--;)
		{
			for (var jj = lines_sort[j].segments.length; jj--;)
			{
				if (min_rast > Math.sqrt(Math.pow(lines_sort[i].segments[ii].point.x - lines_sort[j].segments[jj].point.x, 2) 
							+ Math.pow(lines_sort[i].segments[ii].point.y - lines_sort[j].segments[jj].point.y, 2)))
				{
					min_rast = Math.sqrt(Math.pow(lines_sort[i].segments[ii].point.x - lines_sort[j].segments[jj].point.x, 2) 
							+ Math.pow(lines_sort[i].segments[ii].point.y - lines_sort[j].segments[jj].point.y, 2));
					p1 = ii;
					p2 = jj;
				}
			}
		}
		lines_sort[i].segments[p1].remove();
		lines_sort[i].add(lines_sort[j].segments[p2].point);
	}
	
	g_points = getPathsPoints(lines_sort);
	g_points = changePointsOrderForNaming(g_points, 2);

	for (var i = diag_sort.length; i--;)
	{
		min_rast = 40000;
		for (var j = g_points.length; j--;)
		{
			if (min_rast > Math.sqrt(Math.pow(diag_sort[i].segments[0].point.x - g_points[j].x, 2) 
							+ Math.pow(diag_sort[i].segments[0].point.y - g_points[j].y, 2)))
			{
				min_rast = Math.sqrt(Math.pow(diag_sort[i].segments[0].point.x - g_points[j].x, 2) 
						+ Math.pow(diag_sort[i].segments[0].point.y - g_points[j].y, 2));
				p1 = g_points[j];
			}
		}

		min_rast = 40000;
		for (var j = g_points.length; j--;)
		{
			if (min_rast > Math.sqrt(Math.pow(diag_sort[i].segments[1].point.x - g_points[j].x, 2) 
							+ Math.pow(diag_sort[i].segments[1].point.y - g_points[j].y, 2)))
			{
				min_rast = Math.sqrt(Math.pow(diag_sort[i].segments[1].point.x - g_points[j].x, 2) 
						+ Math.pow(diag_sort[i].segments[1].point.y - g_points[j].y, 2));
				p2 = g_points[j];
			}
		}
		diag_sort[i].removeSegments();
		diag_sort[i].addSegments([p1, p2]);
	}
}

function find_part_chert_on_diag(diag_f, index)
{
	var point_n, point_k;
    var obrs = false;
    var obmen;
    var mass_w = [];
    var mass_d = [];
	g_points = getPathsPoints(lines_sort);
	g_points = changePointsOrderForNaming(g_points, 2);
	for (var key = g_points.length; key--;)
	{
		if (point_ravny(diag_f.segments[0].point, g_points[key]))
		{
			point_n = parseFloat(key);
		}
		if (point_ravny(diag_f.segments[1].point, g_points[key]))
		{
			point_k = parseFloat(key);
		}
	}
	if (point_n > point_k)
	{
		obmen = point_n;
		point_n = parseFloat(point_k);
		point_k = parseFloat(obmen);
	}
	var ni;
	var i = +point_n;
	while (i !== point_k)
	{
		if (+i === g_points.length - 1)
		{
			ni = 0;
		}
		else
		{
			ni = +i + 1;
		}
		var j = ni;
		while (true)
		{
			for (var key = 0; key < lines_sort.length; key++)
			{
				if (point_ravny(lines_sort[key].segments[0].point, g_points[i]) && point_ravny(lines_sort[key].segments[1].point, g_points[j])
					|| point_ravny(lines_sort[key].segments[1].point, g_points[i]) && point_ravny(lines_sort[key].segments[0].point, g_points[j]))
				{
					if (!in_array(mass_w, lines_sort[key]))
					{
    					mass_w.push(lines_sort[key]);
    				}
				}
			}
			for (var key = 0; key < diag_sort.length; key++)
			{
				if (point_ravny(diag_sort[key].segments[0].point, g_points[i]) && point_ravny(diag_sort[key].segments[1].point, g_points[j])
					|| point_ravny(diag_sort[key].segments[1].point, g_points[i]) && point_ravny(diag_sort[key].segments[0].point, g_points[j]))
				{
					if (diag_sort[key] === diag_sort[index])
					{
						obrs = true;
						break;
					}
					else
					{
    					if (!in_array(mass_d, diag_sort[key]) && diag_f !== diag_sort[key])
    					{
	    					mass_d.push(diag_sort[key]);
	    				}
    				}
				}
			}
			if (obrs)
			{
				break;
			}
			if (+j === point_k)
			{
				break;
			}
			if (+j === g_points.length - 1)
    		{
    			j = 0;
    		}
    		else
    		{
    			+j++;
    		}
		}
		if (obrs)
		{
			break;
		}
		if (+i === g_points.length - 1)
		{
			i = 0;
		}
		else
		{
			+i++;
		}
	}

	if (obrs)
	{
		mass_w = [];
		mass_d = [];
		var i = +point_k;
		while (i !== point_n)
    	{
    		if (+i === g_points.length - 1)
    		{
    			ni = 0;
    		}
    		else
    		{
    			ni = +i + 1;
    		}
    		var j = ni;
    		while (true)
    		{
    			for (var key = 0; key < lines_sort.length; key++)
    			{
    				if (point_ravny(lines_sort[key].segments[0].point, g_points[i]) && point_ravny(lines_sort[key].segments[1].point, g_points[j])
    					|| point_ravny(lines_sort[key].segments[1].point, g_points[i]) && point_ravny(lines_sort[key].segments[0].point, g_points[j]))
    				{
    					if (!in_array(mass_w, lines_sort[key]))
    					{
	    					mass_w.push(lines_sort[key]);
	    				}
    				}
    			}
    			for (var key = 0; key < diag_sort.length; key++)
    			{
    				if (point_ravny(diag_sort[key].segments[0].point, g_points[i]) && point_ravny(diag_sort[key].segments[1].point, g_points[j])
    					|| point_ravny(diag_sort[key].segments[1].point, g_points[i]) && point_ravny(diag_sort[key].segments[0].point, g_points[j]))
    				{
	    				if (!in_array(mass_d, diag_sort[key]) && diag_f !== diag_sort[key])
    					{
	    					mass_d.push(diag_sort[key]);
	    				}
    				}
    			}
    			if (+j === point_n)
				{
					break;
				}
				if (+j === g_points.length - 1)
	    		{
	    			j = 0;
	    		}
	    		else
	    		{
	    			+j++;
	    		}
    		}
			if (+i === g_points.length - 1)
    		{
    			i = 0;
    		}
    		else
    		{
    			+i++;
    		}
    	}
	}
	return {mass_w: mass_w, mass_d: mass_d};
}

function in_array(array, item)
{
	for (var key in array)
	{
		if (array[key] === item)
		{
			return true;
		}
	}
	return false;
}

function wheel_zoom(e)
{
	if (e.wheelDelta < 0 && view.zoom > 0.2)
	{
		view.zoom = new Decimal(view.zoom).minus(0.05).toNumber();
		if (view.zoom < 1)
		{
			for (var key = text_points.length; key--;)
			{
				text_points[key].fontSize += 2;
			}
			for (var key in text_lines)
			{
				text_lines[key].fontSize += 2;
			}
			for (var key = text_diag.length; key--;)
			{
				text_diag[key].fontSize += 2;
			}
			if (view.zoom < 0.4)
			{
				for (var key = text_points.length; key--;)
				{
					text_points[key].fontSize += 4;
				}
				for (var key in text_lines)
				{
					text_lines[key].fontSize += 4;
				}
				for (var key = text_diag.length; key--;)
				{
					text_diag[key].fontSize += 4;
				}
			}
		}
	}
	else if (e.wheelDelta > 0 && view.zoom < 2)
	{
		view.zoom = new Decimal(view.zoom).plus(0.05).toNumber();
		if (view.zoom < 1)
		{
			for (var key = text_points.length; key--;)
			{
				text_points[key].fontSize -= 2;
			}
			for (var key in text_lines)
			{
				text_lines[key].fontSize -= 2;
			}
			for (var key = text_diag.length; key--;)
			{
				text_diag[key].fontSize -= 2;
			}
			if (view.zoom < 0.4)
			{
				for (var key = text_points.length; key--;)
				{
					text_points[key].fontSize -= 4;
				}
				for (var key in text_lines)
				{
					text_lines[key].fontSize -= 4;
				}
				for (var key = text_diag.length; key--;)
				{
					text_diag[key].fontSize -= 4;
				}
			}
		}
	}

	if (view.zoom === 1)
	{
		for (var key = text_points.length; key--;)
		{
			text_points[key].fontSize = 16;
		}
		for (var key in text_lines)
		{
			text_lines[key].fontSize = 16;
		}
		for (var key = text_diag.length; key--;)
		{
			text_diag[key].fontSize = 16;
		}
	}

	for (var key in lines)
	{
		lines[key].segments[0].point.x = parseFloat(lines[key].segments[0].point.x.toFixed(2));
		lines[key].segments[1].point.x = parseFloat(lines[key].segments[1].point.x.toFixed(2));
		lines[key].segments[0].point.y = parseFloat(lines[key].segments[0].point.y.toFixed(2));
		lines[key].segments[1].point.y = parseFloat(lines[key].segments[1].point.y.toFixed(2));
	}
	if (start_point !== undefined && end_point !== undefined)
	{
		start_point.x = parseFloat(start_point.x.toFixed(2));
		start_point.y = parseFloat(start_point.y.toFixed(2));
		end_point.x = parseFloat(end_point.x.toFixed(2));
		end_point.y = parseFloat(end_point.y.toFixed(2));
	}
}

var rast;
function touch_zoom(event)
{
	if (event.touches.length > 1)
	{
		if (touch1 === undefined || touch2 === undefined)
		{
			touch1 = new Point(event.touches[0].pageX, event.touches[0].pageY);
			touch2 = new Point(event.touches[1].pageX, event.touches[1].pageY);
			rast = Math.sqrt(Math.pow(touch2.x - touch1.x, 2) + Math.pow(touch2.y - touch1.y, 2));
		}
		else
		{
			rast2 = Math.sqrt(Math.pow(event.touches[1].pageX - event.touches[0].pageX, 2) + Math.pow(event.touches[1].pageY - event.touches[0].pageY, 2));
			if (rast2 < rast && view.zoom > 0.2)
			{
				view.zoom = new Decimal(view.zoom).minus(0.05).toNumber();
				if (view.zoom < 1)
				{
					for (var key = text_points.length; key--;)
					{
						text_points[key].fontSize += 2;
					}
					for (var key in text_lines)
					{
						text_lines[key].fontSize += 2;
					}
					for (var key = text_diag.length; key--;)
					{
						text_diag[key].fontSize += 2;
					}
					if (view.zoom < 0.4)
					{
						for (var key = text_points.length; key--;)
						{
							text_points[key].fontSize += 4;
						}
						for (var key in text_lines)
						{
							text_lines[key].fontSize += 4;
						}
						for (var key = text_diag.length; key--;)
						{
							text_diag[key].fontSize += 4;
						}
					}
				}
			}
			else if (rast2 > rast && view.zoom < 2)
			{
				view.zoom = new Decimal(view.zoom).plus(0.05).toNumber();
				if (view.zoom < 1)
				{
					for (var key = text_points.length; key--;)
					{
						text_points[key].fontSize -= 2;
					}
					for (var key in text_lines)
					{
						text_lines[key].fontSize -= 2;
					}
					for (var key = text_diag.length; key--;)
					{
						text_diag[key].fontSize -= 2;
					}
					if (view.zoom < 0.4)
					{
						for (var key = text_points.length; key--;)
						{
							text_points[key].fontSize -= 4;
						}
						for (var key in text_lines)
						{
							text_lines[key].fontSize -= 4;
						}
						for (var key = text_diag.length; key--;)
						{
							text_diag[key].fontSize -= 4;
						}
					}
				}
			}

			if (view.zoom === 1)
			{
				for (var key = text_points.length; key--;)
				{
					text_points[key].fontSize = 16;
				}
				for (var key in text_lines)
				{
					text_lines[key].fontSize = 16;
				}
				for (var key = text_diag.length; key--;)
				{
					text_diag[key].fontSize = 16;
				}
			}

			rast = Math.sqrt(Math.pow(event.touches[1].pageX - event.touches[0].pageX, 2) + Math.pow(event.touches[1].pageY - event.touches[0].pageY, 2));
		}
		for (var key in lines)
		{
			lines[key].segments[0].point.x = parseFloat(lines[key].segments[0].point.x.toFixed(2));
			lines[key].segments[1].point.x = parseFloat(lines[key].segments[1].point.x.toFixed(2));
			lines[key].segments[0].point.y = parseFloat(lines[key].segments[0].point.y.toFixed(2));
			lines[key].segments[1].point.y = parseFloat(lines[key].segments[1].point.y.toFixed(2));
		}
		if (start_point !== undefined && end_point !== undefined)
		{
			start_point.x = parseFloat(start_point.x.toFixed(2));
			start_point.y = parseFloat(start_point.y.toFixed(2));
			end_point.x = parseFloat(end_point.x.toFixed(2));
			end_point.y = parseFloat(end_point.y.toFixed(2));
		}
	}
}

var first_click = false;

function clicks()
{
	var polotna = fun_canv.getGreeting();

        js_polotna = JSON.parse(polotna);
        width_polotna = js_polotna;

    	 if (width_polotna.length === 0)
    	 {
            AndroidFunction.func_back(1);
    	 }

	document.getElementById('cancelLastAction').onclick = cancelLastAction;
	document.getElementById('reset').onclick = clear_elem;
	elem_button_ok = document.getElementById('ok');
	elem_nums.push(elem_button_ok);
	elem_button_ok.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_button_ok.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_button_ok);
		ok_enter_all();
	};
	document.onwheel = wheel_zoom;
	document.getElementById('myCanvas').addEventListener('touchmove', touch_zoom, false);
	document.getElementById('close_sketch').onclick = close_sketch_click_all;
	elem_jform_n4 = document.getElementById('jform_n4');
	elem_jform_n5 = document.getElementById('jform_n5');
	elem_jform_n9 = document.getElementById('jform_n9');
	elem_useGrid = document.getElementById('useGrid');
	elem_window = document.getElementById('window');
	elem_newLength = document.getElementById('newLength');
	elem_preloader = document.getElementById('preloader');

	var elem_num_0 = document.getElementById('num0');
	elem_nums.push(elem_num_0);
	elem_num_0.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_0.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_0);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "0";
	};
	var elem_num_1 = document.getElementById('num1');
	elem_nums.push(elem_num_1);
	elem_num_1.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_1.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_1);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "1";
	};
	var elem_num_2 = document.getElementById('num2');
	elem_nums.push(elem_num_2);
	elem_num_2.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_2.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_2);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "2";
	};
	var elem_num_3 = document.getElementById('num3');
	elem_nums.push(elem_num_3);
	elem_num_3.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_3.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_3);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "3";
	};
	var elem_num_4 = document.getElementById('num4');
	elem_nums.push(elem_num_4);
	elem_num_4.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_4.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_4);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "4";
	};
	var elem_num_5 = document.getElementById('num5');
	elem_nums.push(elem_num_5);
	elem_num_5.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_5.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_5);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "5";
	};
	var elem_num_6 = document.getElementById('num6');
	elem_nums.push(elem_num_6);
	elem_num_6.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_6.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_6);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "6";
	};
	var elem_num_7 = document.getElementById('num7');
	elem_nums.push(elem_num_7);
	elem_num_7.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_7.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_7);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "7";
	};
	var elem_num_8 = document.getElementById('num8');
	elem_nums.push(elem_num_8);
	elem_num_8.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_8.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_8);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "8";
	};
	var elem_num_9 = document.getElementById('num9');
	elem_nums.push(elem_num_9);
	elem_num_9.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_num_9.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_num_9);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += "9";
	};
	var elem_numback = document.getElementById('numback');
	elem_nums.push(elem_numback);
	elem_numback.onclick = function()
	{
		clearInterval(timer_button_color);
		for (var key = elem_nums.length; key--;)
		{
			elem_nums[key].style.background = '';
		}
		elem_numback.style.background = '#E0FFFF';
		timer_button_color = setInterval(sbros_but_color, 1000, elem_numback);
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value = elem_newLength.value.slice(0, -1);
	};

	document.onkeydown = function(e)
	{
		if (elem_window.style.display === "" || elem_window.style.display === "none")
		{
			return;
		}

		if (e.keyCode === 13)
		{
			ok_enter_all();
		}
		else if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}

		switch (e.keyCode)
		{
			case 48:
				elem_newLength.value += "0";
			break;
			case 49:
				elem_newLength.value += "1";
			break;
			case 50:
				elem_newLength.value += "2";
			break;
			case 51:
				elem_newLength.value += "3";
			break;
			case 52:
				elem_newLength.value += "4";
			break;
			case 53:
				elem_newLength.value += "5";
			break;
			case 54:
				elem_newLength.value += "6";
			break;
			case 55:
				elem_newLength.value += "7";
			break;
			case 56:
				elem_newLength.value += "8";
			break;
			case 57:
				elem_newLength.value += "9";
			break;
			case 46:
				elem_newLength.value = "";
			break;
			case 8:
				elem_newLength.value = elem_newLength.value.slice(0, -1);
			break;
			case 96:
				elem_newLength.value += "0";
			break;
			case 97:
				elem_newLength.value += "1";
			break;
			case 98:
				elem_newLength.value += "2";
			break;
			case 99:
				elem_newLength.value += "3";
			break;
			case 100:
				elem_newLength.value += "4";
			break;
			case 101:
				elem_newLength.value += "5";
			break;
			case 102:
				elem_newLength.value += "6";
			break;
			case 103:
				elem_newLength.value += "7";
			break;
			case 104:
				elem_newLength.value += "8";
			break;
			case 105:
				elem_newLength.value += "9";
			break;
		}
	};
	document.oncontextmenu = function()
	{
		if (elem_useGrid.checked)
		{
			elem_useGrid.checked = false;
		}
		return false;
	};
}

function sort_sten()
{
	g_points = getPathsPoints(lines);
	g_points = changePointsOrderForNaming(g_points, 2);
	var j;
	for (var i = 0; i < g_points.length; i++)
	{
		if (i === g_points.length - 1)
		{
			j = 0;
		}
		else
		{
			j = i + 1;
		}
		for (var key in lines)
		{
			if (point_ravny(lines[key].segments[0].point, g_points[i]) && point_ravny(lines[key].segments[1].point, g_points[j])
				|| point_ravny(lines[key].segments[1].point, g_points[i]) && point_ravny(lines[key].segments[0].point, g_points[j]))
			{
				lines_sort.push(lines[key]);
				break;
			}
		}
	}
}


function drawLabels()
{
    var textPoints = [];
    var pt, id1, id2;
    g_points = [];
    text_points_lines_id = [];
    var namedPoints = createVertexNames();
    for(var i=0;i<namedPoints.length;i++){
        if(namedPoints[i].point)
        {
        	g_points.push(new Point(namedPoints[i].point.x, namedPoints[i].point.y));
            pt = new PointText(
                {
                    point: new Point(namedPoints[i].point.x - 10,namedPoints[i].point.y - 5),
                    content:namedPoints[i].name,
                    fillColor: 'blue',
                    justification: 'center',
                    fontFamily: 'TimesNewRoman',
                    fontWeight: 'bold',
                    fontSize : 16
                });
            textPoints.push(pt);
            for (var key in lines)
            {
            	for (var j in lines[key].segments)
            	{
            		if (point_ravny(namedPoints[i].point, lines[key].segments[j].point))
            		{
            			id1 = key;
            		}
            	}
            }
            for (var key in lines)
            {
            	if (key === id1)
            	{
            		continue;
            	}
            	for (var j in lines[key].segments)
            	{
            		if (point_ravny(namedPoints[i].point, lines[key].segments[j].point))
            		{
            			id2 = key;
            		}
            	}
            }
            text_points_lines_id.push({id_pt: pt.id, id_line1: id1, id_line2: id2});
        }
    }
    var f = 1;
	while (f > view.zoom)
	{
		f -= 0.05;
		if (f < 1)
		{
			for (var i = textPoints.length; i--;)
			{
				textPoints[i].fontSize += 2;
			}
			if (f < 0.4)
			{
				for (var i = textPoints.length; i--;)
				{
					textPoints[i].fontSize += 4;
				}
			}
		}
	}
    return textPoints;
}

function createVertexNames()
{
    var namedPoints = [];
    var allpoints = changePointsOrderForNaming(getPathsPoints(lines), 1);
    if (allpoints !== undefined)
    {
        for (var i = 0; i < allpoints.length; i++)
        {
        	if (code === 90)
        	{
        		code = 64;
        		alfavit++;
        	}
        	code++;
        	if (alfavit === 0)
        	{
	            namedPoints.push({point: allpoints[i], name: String.fromCharCode(code)});
	        }
	        else
	        {
	        	namedPoints.push({point: allpoints[i], name: String.fromCharCode(code) + alfavit});
	        }
        }
    }
    return namedPoints;
}

function findWallsByPoint(points,point,flag)
{
	var temp_point = [];
	var j = 0, min_y, rez_point;
	if (flag === 1)
	{
		for(var j in lines)
		{
			if (point_ravny(lines[j].getFirstSegment().point, point) || point_ravny(lines[j].getLastSegment().point, point)) {
				if (!point_ravny(lines[j].getFirstSegment().point, point) && lines[j].getFirstSegment().point.x >= point.x) {
					temp_point.push(lines[j].getFirstSegment().point.clone());
				}
				if (!point_ravny(lines[j].getLastSegment().point, point) && lines[j].getLastSegment().point.x >= point.x) {
					temp_point.push(lines[j].getLastSegment().point.clone());
				}
			
			}
		}
		min_y = temp_point[0].y;
		rez_point = temp_point[0];
		for (var key in temp_point)
		{
			if (temp_point[key].y < min_y)
			{
				min_y = temp_point[key].y;
				rez_point = temp_point[key];
			}
		}
		return rez_point;
	}

	if (flag === 2)
	{	
		for(var j in lines)
		{
			if (point_ravny(lines[j].getFirstSegment().point, point) || point_ravny(lines[j].getLastSegment().point, point))
			{
				if (!alreadyExist(points, lines[j].getFirstSegment().point)) {
					return lines[j].getFirstSegment().point.clone();
				}
				if (!alreadyExist(points, lines[j].getLastSegment().point)) {
					return lines[j].getLastSegment().point.clone();
				}
			}
				
		}
	}
}

function changePointsOrderForNaming(points, flag)
{
	if (flag === 1)
	{
	    if(findMinAndMaxCordinate()!==undefined){
	        var minX = findMinAndMaxCordinate().minX;
	        var minY = findMinAndMaxCordinate().minY;
	    }
	}
	else if (flag === 2)
	{
		if(findMinAndMaxCordinate_g()!==undefined){
	        var minX = findMinAndMaxCordinate_g().minX;
	        var minY = findMinAndMaxCordinate_g().minY;
		}
	}
    if((minX!==undefined)&&(minY!==undefined)){
        var min_y, min_q, point, result = [];

        if (flag === 1)
		{
	    	min_y = findMinAndMaxCordinate().maxY;
	    }
	    else if (flag === 2)
		{
			min_y = findMinAndMaxCordinate_g().maxY;
		}
    	min_q = 0;
    	for (var q in points)
    	{
            if (points[q].x > minX - 0.5 && points[q].x < minX + 0.5 && points[q].y <= min_y)
            {
            	min_y = points[q].y;
                min_q = q;
            }
        }
        point = points[min_q].clone();
        points.splice(min_q, 1);
        result.push(point);

        while (points.length !== 0)
        {
            if (result.length === 1)
            {
                point = findWallsByPoint(result, result[result.length - 1], 1);
                points.splice(points.indexOf(point),1);
                result.push(point);
            }
            else
            {
                point = findWallsByPoint(result, result[result.length - 1], 2);
                points.splice(points.indexOf(point),1);
                result.push(point);
            }

        }
        return result;
    }
}

function getPathsPoints(lines)
{
    var allpoints = [];
    for (var j in lines)
    {
        for (var k = 0; k < lines[j].segments.length; k++)
        {
            allpoints.push(lines[j].segments[k].point.clone());
        }
    }
    return unique(allpoints);
}

function unique(arr)
{
    var result = [], str;
    nextInput:
        for (var i = 0; i < arr.length; i++)
        {
            str = arr[i].clone();
            for (var j = 0; j < result.length; j++)
            {
                if (point_ravny(result[j], str)) continue nextInput;
            }
            result.push(str);
        }

    return result;
}

function findMinAndMaxCordinate()
{
    if(points.length>0){
        var minX = points[0].x;
        var maxX = points[0].x;
        var minY = points[0].y;
        var maxY = points[0].y;
        for(var i=1;i<points.length;i++){
            if(points[i].x<minX){
                minX = points[i].x;
            }
            if(points[i].x>maxX){
                maxX = points[i].x;
            }
            if(points[i].y<minY){
                minY = points[i].y;
            }
            if(points[i].y>maxY){
                maxY = points[i].y;
            }
        }
        return {minX:minX,maxX:maxX,minY:minY,maxY:maxY};
    }
}

function alreadyExist(arr,value)
{
    var result = false;
    for(var i = 0; i < arr.length; i++)
    {
        if(point_ravny(arr[i], value))
        {
            result = true;
        }
    }
    return result;
}

function get_angle(center, point)
{
    var x = new Decimal(point.x).minus(center.x).toNumber();
    var y = new Decimal(point.y).minus(center.y).toNumber();
    if(x === 0)
	{
		return (y > 0) ? 180 : 0;
	}
    var a = Decimal.atan(new Decimal(y).dividedBy(x)).times(180).dividedBy(Decimal.acos(-1));
    a = (x > 0) ? a.plus(90).toNumber() : a.plus(270).toNumber();
    return a;
}

function migalka(path)
{
	if (path.strokeColor.red === 1)
	{
		path.strokeColor = 'blue';
	}
	else
	{
		path.strokeColor = 'red';
	}
}

function sbros_but_color(elem)
{
	elem.style.background = '';
	clearInterval(timer_button_color);
}

var square_tkan;
function tkan()
{
	var polygonVertices = [], polygons, paddingPolygon, x1, y1, x2, y2, intersections;
	for(var i = g_points.length; i--;)
	{
		polygonVertices.push({x: g_points[i].x, y: g_points[i].y});
	}
	polygons = init_pol(polygonVertices);

	paddingPolygon = polygons.paddingPolygon;

	var lines_tkan = [];
	var chertezh = new Path();
	for(var i = paddingPolygon.edges.length; i--;)
	{
		x1 = paddingPolygon.edges[i].vertex1.x;
		y1 = paddingPolygon.edges[i].vertex1.y;
		x2 = paddingPolygon.edges[i].vertex2.x;
		y2 = paddingPolygon.edges[i].vertex2.y;
		lines_tkan.push(Path.Line(new Point(x1, y1), new Point(x2, y2)));
		lines_tkan[lines_tkan.length - 1].strokeColor = 'red';
	}
	var min_up_y = (lines_tkan[0].segments[0].point.y + lines_tkan[0].segments[1].point.y) / 2;
	var m, start_line = lines_tkan[0];
	for(var i = lines_tkan.length; i--;)
	{
		m = (lines_tkan[i].segments[0].point.y + lines_tkan[i].segments[1].point.y) / 2;
		if (min_up_y > m)
		{
			min_up_y = m;
			start_line = lines_tkan[i];
		}
	}
	
	var last_elem;
	while(lines_tkan[lines_tkan.length - 1] !== start_line)
	{
		last_elem = lines_tkan[lines_tkan.length - 1];
		for(var i = lines_tkan.length - 1; i--;)
		{
			lines_tkan[i + 1] = lines_tkan[i];
		}
		lines_tkan[0] = last_elem;
	}

	var p, ic;
	for(var i = lines_tkan.length; i--;)
	{
		ic = i;
		for(var j = i; j--;)
		{
			if (i === lines_tkan.length - 1 && j === 0)
			{
				break;
			}
			intersections = lines_tkan[i].getIntersections(lines_tkan[j]);
			if (intersections.length !== 0)
			{
				p = intersections[0].point;
				ic = j;
			}
		}
		if (ic !== i)
		{
			chertezh.add(p);
			i = ic + 1;
		}
	}
	intersections = lines_tkan[0].getIntersections(lines_tkan[lines_tkan.length - 1]);
	if (intersections.length !== 0)
	{
		chertezh.add(intersections[0].point);
	}
	chertezh.closed = true;
	chertezh.fillColor = 'green';
	chertezh.strokeColor = 'blue';
	//chertezh.selected = true;
	chertezh.opacity = 0.3;

	for(var i = lines_tkan.length; i--;)
	{
		lines_tkan[i].remove();
	}
	lines_tkan = [];
	var seg0, seg1;
	for(var i = chertezh.segments.length; i--;)
	{
		seg0 = chertezh.segments[(i + chertezh.segments.length - 1) % chertezh.segments.length].point.clone();
		seg1 = chertezh.segments[i].point.clone();
		lines_tkan.push(Path.Line(seg0, seg1));
	}
	add_text_contur(lines_tkan);
	square_tkan = new Decimal(Math.abs(chertezh.area)).dividedBy(10000).toNumber();
	//console.log(square_tkan);

	var min_len = 1000;
	for(var i = chertezh.segments.length; i--;)
	{
		m = Math.sqrt(Math.pow(chertezh.segments[i].point.x - text_points[0].point.x, 2) 
				+ Math.pow(chertezh.segments[i].point.y - text_points[0].point.y, 2));
		if (m < min_len)
		{
			min_len = m;
			ic = i;
		}
	}

	for(var key in text_lines)
	{
		text_lines[key].remove();
	}
	for(var key in text_points)
	{
		text_points[key].remove();
	}
	for(var key in text_diag)
	{
		text_diag[key].remove();
	}
	text_points = [];

	var c_i = 0, pt_name, pt, p;
	alfavit = 0;
	code = 64;
	text_points_lines_id = [];
	while(c_i < chertezh.segments.length)
	{
		p = chertezh.segments[ic].point;
		ic = (ic + 1) % chertezh.segments.length;
		if (code === 90)
    	{
    		code = 64;
    		alfavit++;
    	}
    	code++;
    	if (alfavit === 0)
    	{
            pt_name = String.fromCharCode(code);
        }
        else
        {
        	pt_name = String.fromCharCode(code) + alfavit;
        }
		pt = new PointText(
        {
            point: new Point(p.x, p.y),
            content: pt_name,
            fillColor: 'black',
            justification: 'center',
            fontFamily: 'TimesNewRoman',
            fontWeight: 'bold',
            fontSize: 12
        });
    	text_points.push(pt);
    	
    	for (var i = lines_tkan.length; i--;)
        {
        	for (var j = lines_tkan[i].segments.length; j--;)
        	{
        		if (point_ravny(p, lines_tkan[i].segments[j].point))
        		{
        			id1 = lines_tkan[i].id;
        		}
        	}
        }
        for (var i in lines_tkan)
        {
        	if (lines_tkan[i].id === id1)
        	{
        		continue;
        	}
        	for (var j in lines_tkan[i].segments)
        	{
        		if (point_ravny(p, lines_tkan[i].segments[j].point))
        		{
        			id2 = lines_tkan[i].id;
        		}
        	}
        }
        text_points_lines_id.push({id_pt: pt.id, id_line1: id1, id_line2: id2});
		c_i++;
	}

	var angle_final, j_f, sq_min = 100222, sq_polo, sq_obr;
	for (var i = lines_tkan.length; i--;)
	{
		lines_tkan[i].rotate(-1, view.center);
	}
	for (var angle_rotate = 0; angle_rotate < 181; angle_rotate++)
	{
		for (var i = lines_tkan.length; i--;)
		{
			lines_tkan[i].rotate(1, view.center);
		}
		for (var j = 0; j < width_polotna.length; j++)
		{
			g_points = getPathsPoints(lines_tkan);
			var points_m = findMinAndMaxCordinate_g();
			if (width_polotna[j].width < Math.abs(points_m.maxY - points_m.minY))
			{
				continue;
			}
			add_polotno_tkan(width_polotna[j].width);
			sq_polo = new Decimal(polotna[0].down.length).times(polotna[0].left.length);
			sq_polo = sq_polo.dividedBy(10000).toNumber();
			sq_obr = new Decimal(sq_polo).minus(square_tkan).toNumber();
			sq_obr = +sq_obr.toFixed(2);
			
			if (sq_obr < sq_min)
			{
				sq_min = sq_obr;
				angle_final = angle_rotate;
				j_f = j;
				//console.log(sq_obr, angle_final);
			}
		}
	}
	for (var i = lines_tkan.length; i--;)
	{
		lines_tkan[i].rotate(180, view.center);
	}
	if (j_f === undefined)
	{
		alert('Ошибка! Этот чертеж не вместится ни в одно доступное полотно.');
		chertezh.remove();
		for(var i = lines_tkan.length; i--;)
		{
			lines_tkan[i].remove();
		}
		lines_tkan = [];
		for (var key = text_contur.length; key--;)
	    {
	    	text_contur[key].remove();
	    }
	    text_contur = [];
		clear_elem();
		return false;
	}
	for (var i = lines_tkan.length; i--;)
	{
		lines_tkan[i].rotate(angle_final, view.center);
	}
	chertezh.rotate(angle_final, view.center);
	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].rotate(angle_final, view.center);
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].rotate(angle_final, view.center);
	}
	g_points = getPathsPoints(lines_tkan);
	add_polotno_tkan(width_polotna[j_f].width);

	for (var key = text_contur.length; key--;)
    {
    	text_contur[key].remove();
    }
    text_contur = [];
    
    add_text_contur(lines_tkan);

    var j = 0;
    for (var i = 0; i < lines_tkan.length; i++)
    {
    	if (i === 0)
    	{
    		j = lines_tkan.length - 1;
    	}
    	else
    	{
    		j = i - 1;
    	}
    	if (lines_tkan[i] !== lines_tkan[j] && obshaya_point(lines_tkan[i], lines_tkan[j]) !== null)
    	{
    		moveVertexNameContur(lines_tkan[i], lines_tkan[j], obshaya_point(lines_tkan[i], lines_tkan[j]));
    	}
    }

	//get_koordinats_poloten(p_usadki);
	sq_polo = new Decimal(polotna[0].down.length).times(polotna[0].left.length);
	sq_polo = sq_polo.dividedBy(10000).toNumber();
	sq_obr = new Decimal(sq_polo).minus(square_tkan).toNumber();
	square_obrezkov = +sq_obr.toFixed(2);
	//console.log('sq_obr', square_obrezkov);

	width_final = width_polotna[j_f].width;
	//console.log(width_final, "width_polotna");

	polotna[0].up.strokeColor = 'red';
	polotna[0].up.dashArray = [10, 4];
	polotna[0].down.strokeColor = 'red';
	polotna[0].down.dashArray = [10, 4];
	polotna[0].left.strokeColor = 'red';
	polotna[0].left.dashArray = [10, 4];
	polotna[0].right.strokeColor = 'red';
	polotna[0].right.dashArray = [10, 4];

	koordinats_poloten = [];
	koordinats_poloten[0] = [];
	for (var j = text_points.length; j--;)
	{
		kx = Decimal.abs(new Decimal(text_points[j].point.x).minus(polotna[0].left.position.x)).toNumber(); 
		ky = Decimal.abs(new Decimal(text_points[j].point.y).minus(polotna[0].down.position.y)).toNumber();
		kx = +kx.toFixed(1);
		ky = +ky.toFixed(1);
		koordinats_poloten[0][j] = {name: text_points[j].content, koordinats: "(" + kx + ", " + ky + ")"};
	}
	return true;
}

function add_polotno_tkan(p_width)
{
	for (var key = polotna.length; key--;)
	{
		polotna[key].left.remove();
		polotna[key].right.remove();
		polotna[key].up.remove();
		polotna[key].down.remove();
	}
	polotna = [];
	var points_m = findMinAndMaxCordinate_g();
	var line_left = Path.Line(new Point(points_m.minX, points_m.maxY), new Point(points_m.minX, new Decimal(points_m.maxY).minus(p_width).toNumber()));
	var line_right = Path.Line(new Point(points_m.maxX, points_m.maxY), new Point(points_m.maxX, new Decimal(points_m.maxY).minus(p_width).toNumber()));
	var line_up = Path.Line(line_left.segments[1].point, line_right.segments[1].point);
	var line_down = Path.Line(line_left.segments[0].point, line_right.segments[0].point);
	polotna[0] = {up: line_up, down: line_down, left: line_left, right: line_right};
}

var text_contur = [];
function add_text_contur(arr)
{
	for(var i = arr.length; i--;)
	{
		if (arr[i].length > 30)
		{
			var tc = new PointText();

			tc.fontWeight = 'bold';
			tc.fontSize = 12;
			var f = 1;
			while (f > view.zoom)
			{
				f -= 0.05;
				if (f < 1)
				{
					tc.fontSize += 2;
					if (f < 0.4)
					{
						tc.fontSize += 4;
					}
				}
			}
			tc.content = Math.round(arr[i].length);
			var angle = (Math.atan((arr[i].segments[1].point.y-arr[i].segments[0].point.y)/(arr[i].segments[1].point.x
				-arr[i].segments[0].point.x))*180)/Math.PI;
		    tc.rotate(angle);
		    tc.position = arr[i].position;
		    text_contur.push(tc);
		}
	}
}

function moveVertexNameContur(line1, line2, newPoint)
{
	var pt_id;
	for (var key in text_points_lines_id)
	{
		if (+text_points_lines_id[key].id_line1 === line1.id && +text_points_lines_id[key].id_line2 === line2.id
			|| +text_points_lines_id[key].id_line2 === line1.id && +text_points_lines_id[key].id_line1 === line2.id)
		{
			pt_id = text_points_lines_id[key].id_pt;
			break;
		}
	}

	for (var key = text_points.length; key--;)
	{
		if (text_points[key].id === +pt_id)
		{
	    	text_points[key].point = new Point(newPoint.x, newPoint.y);
	    	return key;
		}
	}
}

var cuts = [];

function find_obrezki_1pol(lines)
{
	g_points = getPathsPoints(lines);
	g_points = changePointsOrderForNaming(g_points, 2);
	var points_m = findMinAndMaxCordinate_g();
	var first = 0;
	var temp, hitResults, gr_polotna1, gr_polotna2;
	for(var i = g_points.length; i--;)
	{
		if (g_points[i].y === points_m.maxY)
		{
			first = i;
			break;
		}
	}
	while(first < g_points.length - 1)
	{
		temp = g_points[g_points.length - 1].clone();
		for(var i = g_points.length - 1; i--;)
		{
			g_points[i + 1] = g_points[i];
		}
		g_points[0] = temp.clone();
		first++;
	}

	var bool_kray, line1, line2, op;
	for(var i = g_points.length; i--;)
	{
		bool_kray = false;
		hitResults = project.hitTestAll(g_points[i], {class: Path, segments: true, stroke: true, tolerance: 10});

		for(var j = hitResults.length; j--;)
		{
			if (hitResults[j].item === polotna[0].up || hitResults[j].item === polotna[0].down ||
				hitResults[j].item === polotna[0].left || hitResults[j].item === polotna[0].right)
			{
				bool_kray = true;
				break;
			}
		}
		if (bool_kray)
		{
			cuts.push(new Path);
			cuts[cuts.length - 1].add(g_points[i].clone());
			if (cuts.length > 1)
			{
				cuts[cuts.length - 2].add(g_points[i].clone());
			}
		}
		else
		{
			cuts[cuts.length - 1].add(g_points[i].clone());
		}
	}
	cuts[cuts.length - 1].add(g_points[g_points.length - 1].clone());

	for(var i = cuts.length; i--;)
	{
		gr_polotna1 = [];
		gr_polotna2 = [];
		hitResults = project.hitTestAll(cuts[i].segments[0].point, {class: Path, segments: true, stroke: true, tolerance: 10});
		for(var j = hitResults.length; j--;)
		{
			if (hitResults[j].item === polotna[0].up || hitResults[j].item === polotna[0].down ||
				hitResults[j].item === polotna[0].left || hitResults[j].item === polotna[0].right)
			{
				gr_polotna1.push(hitResults[j].item);
			}
		}
		hitResults = project.hitTestAll(cuts[i].segments[cuts[i].segments.length - 1].point, {class: Path, segments: true, stroke: true, tolerance: 10});
		for(var j = hitResults.length; j--;)
		{
			if (hitResults[j].item === polotna[0].up || hitResults[j].item === polotna[0].down ||
				hitResults[j].item === polotna[0].left || hitResults[j].item === polotna[0].right)
			{
				gr_polotna2.push(hitResults[j].item);
			}
		}
		var bool_del = false;
		for(var j1 = gr_polotna1.length; j1--;)
		{
			for(var j2 = gr_polotna2.length; j2--;)
			{
				if (gr_polotna1[j1] === gr_polotna2[j2] && cuts[i].segments.length === 2)
				{
					cuts.splice(i, 1);
					bool_del = true;
					break;
				}
			}
		}
		if (!bool_del)
		{
			op = null;
			for(var j1 = gr_polotna1.length; j1--;)
			{
				for(var j2 = gr_polotna2.length; j2--;)
				{
					if (gr_polotna1[j1] === gr_polotna2[j2])
					{
						op = 1;
						break;
					}
					if (obshaya_point(gr_polotna1[j1], gr_polotna2[j2]) !== null)
					{
						op = obshaya_point(gr_polotna1[j1], gr_polotna2[j2]);
						break;
					}
				}
			}
			
			if (op !== 1)
			{
				if (op !== null)
				{
					cuts[i].add(op.clone());
				}
				else
				{
					cuts[i].add(polotna[0].up.segments[0].point.clone());
					cuts[i].add(polotna[0].up.segments[1].point.clone());
				}
			}
			cuts[i].closed = true;
			cuts[i].fillColor = 'green';
			cuts[i].opacity = 0.2;
		}
	}
}

auto = fun_canv.get_auto();

if (auto == 1)
{

var qw = fun_canv.get_walls_points();
       qw = JSON.parse(qw);
       walls_points_rec = qw;

var qw = fun_canv.get_diags_points();
        qw = JSON.parse(qw);
        diags_points_rec = qw;

var qw = fun_canv.get_pt_points();
        qw = JSON.parse(qw);
        pt_points_rec = qw;

	ready = true;
	triangulate_bool = true;
	var obj, pt_name;
	for (var i = walls_points_rec.length; i--;)
	{
		obj = new Path.Line(new Point(walls_points_rec[i].s0_x, walls_points_rec[i].s0_y), 
			new Point(walls_points_rec[i].s1_x, walls_points_rec[i].s1_y));
		obj.strokeColor = 'green';
		obj.strokeWidth = 3;
		lines_sort.push(obj);
		lines[obj.id] = obj;
	}
	for (var i = diags_points_rec.length; i--;)
	{
		obj = new Path.Line(new Point(diags_points_rec[i].s0_x, diags_points_rec[i].s0_y), 
			new Point(diags_points_rec[i].s1_x, diags_points_rec[i].s1_y));
		obj.strokeColor = 'green';
		obj.strokeWidth = 1;
		diag_sort.push(obj);
	}
	var code_char = 64, alfavit_num = 0, id1, id2, op;
	for (var i = 0; i < pt_points_rec.length; i++)
	{
		if (code_char === 90)
    	{
    		code_char = 64;
    		alfavit_num++;
    	}
    	code_char++;
    	if (alfavit_num === 0)
    	{
            pt_name = String.fromCharCode(code_char);
        }
        else
        {
        	pt_name = String.fromCharCode(code_char) + alfavit_num;
        }
		obj = new PointText(
			{
                point: new Point(pt_points_rec[i].x, pt_points_rec[i].y),
                content: pt_name,
                fillColor: 'blue',
                justification: 'center',
                fontFamily: 'TimesNewRoman',
                fontWeight: 'bold',
                fontSize : 16
            });
		text_points.push(obj);

		op = new Point(pt_points_rec[i].x + 10, pt_points_rec[i].y + 5);
		for (var j = lines_sort.length; j--;)
		{
			for (var k = lines_sort.length; k--;)
			{
				if (obshaya_point(lines_sort[j], lines_sort[k]) !== null && lines_sort[j] !== lines_sort[k])
				{
					if (point_ravny(obshaya_point(lines_sort[j], lines_sort[k]), op))
					{
						id1 = lines_sort[j].id;
						id2 = lines_sort[k].id;
						break;
					}
				}
			}
		}
		text_points_lines_id.push({id_pt: obj.id, id_line1: id1, id_line2: id2});
	}

	code = code_char;
	alfavit = alfavit_num;
	zerkalo(1);
	close_sketch_click_all();
}

});