jQuery(document).ready(function()
{
var reg_url = /gm-vrn/;
if (!reg_url.test(location.href))
{
	window.history.back();
}

if (typeof(width_polotna) === "undefined" || typeof(diup) === "undefined" || typeof(url) === "undefined" || typeof(tre) === "undefined"
	|| typeof(col) === "undefined" || typeof(man) === "undefined")
{
	window.history.back();
}

paper.install(window);
paper.setup("myCanvas");
var tool = new Tool();

var elem_newLength, elem_window, elem_useGrid, elem_jform_n4, elem_jform_n5, elem_jform_n9, elem_preloader;
var start_point, end_point, move_point, begin_point, c_point;
var point_start_or_end;
var krug_start, krug_end;
var line_h, line_v;
var lines = [];
var lines_sort =[];
var chert_close = false;
var line_bad = false;
var opred_rez;
var text_lines = [];
var text_diag = [];
var text_v, text_h;
var text_points = [];
var g_points = [];
var diag = [];
var vh = 'v';
var points;
var code = 64, alfavit = 0;
var timer1, timer_mig;
var fix_point_dvig, peredvig;
var touch1, touch2;
var ready = false;
var down_g, right_g, up_g, left_g, granica;
var diag_sort = [];
var width_final, square_obrezkov = 0, angle_final, sq_polotna, cuts_json, p_usadki_final = 1;
var arr_cancel = [], triangulate_rezhim = 0, begin_point_diag, end_point_diag, manual_diag, text_manual_diags = [];
var str_all_lines = "ab=90;bc=176;cd=98;ad=167;ac=222", all_lines_length, lines_length = [];
var seam = 0, code_og, alfavit_og, calc_img, cut_img, seam_lines = [], triangulator_pro = 0;
clicks();
resize_canvas();
jQuery(window).resize(resize_canvas);
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

if (triangulator_pro === 1)
{
	document.getElementById('popup3').style.display = 'block';
	var elem_koordinans = document.getElementById('textarea_koordinats');

	document.getElementById('koordinats_cancel').onclick = function()
	{
		document.getElementById('popup3').style.display = 'none';
	};

	document.getElementById('koordinats_ok').onclick = function()
	{
		build_chert_on_koordinats = build_chert_on_koordinats.process();
		document.getElementById('preloader').style.display = 'block';
		setTimeout(build_chert_on_koordinats, 200);
	};
}

var build_chert_on_koordinats = function()
{
	try
	{
		var arr1 = elem_koordinans.value.replace(/\s+/g, ' ').trim().split(' ');
		var splited_str, splited_str2, name, x, y, chert_koordinats = [];
		var line, pt;

		for (var i = arr1.length; i--;)
		{
			splited_str = arr1[i].split('(');
			name = splited_str[0];
			splited_str[1] = splited_str[1].replace(')', '');
			splited_str2 = splited_str[1].split(';');
			x = +splited_str2[0];
			y = +splited_str2[1];
			chert_koordinats.push({name: name, x: x + 100, y: y + 100});
		}

		for (var i = chert_koordinats.length - 1; i--;)
		{
			line = new Path.Line(new Point(chert_koordinats[i + 1].x, chert_koordinats[i + 1].y),
								new Point(chert_koordinats[i].x, chert_koordinats[i].y));
			line.strokeColor = 'green';
			line.strokeWidth = 3;
			line.data.fixed = true;
			lines.push(line);
			lines_sort.push(line);
			line.data.id = lines.length - 1;

			g_points.push(new Point(chert_koordinats[i + 1].x, chert_koordinats[i + 1].y));
	        pt = new PointText(
	            {
	                point: new Point(chert_koordinats[i + 1].x - 10, chert_koordinats[i + 1].y - 5),
	                content: chert_koordinats[i + 1].name,
	                fillColor: 'blue',
	                justification: 'center',
	                fontFamily: 'lucida console',
	                fontWeight: 'bold',
	                fontSize: (14 / view.zoom).toFixed(2)-0
	            });
	        text_points.push(pt);
		}

		line = new Path.Line(new Point(chert_koordinats[0].x, chert_koordinats[0].y),
			new Point(chert_koordinats[chert_koordinats.length - 1].x, chert_koordinats[chert_koordinats.length - 1].y));
		line.strokeColor = 'green';
		line.strokeWidth = 3;
		line.data.fixed = true;
		lines.push(line);
		lines_sort.push(line);
		line.data.id = lines.length - 1;

		g_points.push(new Point(chert_koordinats[0].x, chert_koordinats[0].y));
	    pt = new PointText(
	        {
	            point: new Point(chert_koordinats[0].x - 10, chert_koordinats[0].y - 5),
	            content: chert_koordinats[0].name,
	            fillColor: 'blue',
	            justification: 'center',
	            fontFamily: 'lucida console',
	            fontWeight: 'bold',
	            fontSize: (14 / view.zoom).toFixed(2)-0
	        });
	    text_points.push(pt);

	    var ptsr, id1, id2;

	    for (var i = text_points.length; i--;)
	    {
	    	ptsr = new Point(text_points[i].point.x + 10, text_points[i].point.y + 5);
	    	for (var key = lines.length; key--;)
	        {
	        	for (var j in lines[key].segments)
	        	{
	        		if (point_ravny(ptsr, lines[key].segments[j].point))
	        		{
	        			id1 = key;
	        		}
	        	}
	        }
	        for (var key = lines.length; key--;)
	        {
	        	if (key === id1)
	        	{
	        		continue;
	        	}
	        	for (var j in lines[key].segments)
	        	{
	        		if (point_ravny(ptsr, lines[key].segments[j].point))
	        		{
	        			id2 = key;
	        		}
	        	}
	        }
	    	text_points[i].data.id_line1 = +id1;
	    	text_points[i].data.id_line2 = +id2;
	    }

	    draw_lines_text();
	    chert_close = true;

	    k = 0;
	    for (var key = g_points.length; key--;)
	    {
	    	k++;
	    }
	    elem_jform_n9.value = k;
		perimetr();

	    triangulator();

		if (diag.length < k - 3)
		{
			for (var key = 3; key--;)
			{
				pulemet();
				//console.log('pulemet');
				if (diag.length === k - 3)
				{
					break;
				}
			}
		}
		if (diag.length < k - 3)
		{
			alert('Ошибка в построении диагоналей!');
			//console.log(diag);
			//console.log(diag_sort);
			elem_preloader.style.display = 'none';
			return;
		}

		diag_sortirovka();

		for (var key = diag_sort.length; key--;)
	    {
	    	diag_sort[key].data.fixed = true;
	    	diag_sort[key].strokeColor = 'green';
	    	add_text_diag(key);
	    }

	    ready = true;
	    triangulate_bool = true;

	    text_points_sdvig();

		document.getElementById('popup3').style.display = 'none';
		//console.log(lines, diag, text_points);
		align_center();
		resize_canvas();
		square();

		save_cancel();
	}
	catch(e)
	{
		alert('Ошибка!');
	}
	document.getElementById('preloader').style.display = 'none';
};

var build_chert = function()
{
	var regexp = /^\d+$/;
	var v1, v2, l;
	str_all_lines = str_all_lines.toUpperCase();
	str_all_lines = str_all_lines.replace(/\s/g, '');
	console.log(str_all_lines);
	all_lines_length = str_all_lines.split(';');
	for (var i = all_lines_length.length; i--;)
	{
		if (regexp.test(all_lines_length[i].substring(1, 2)))
		{
			v1 = all_lines_length[i].substring(0, 2);
			if (regexp.test(all_lines_length[i].substring(3, 4)))
			{
				v2 = all_lines_length[i].substring(2, 4);
			}
			else
			{
				v2 = all_lines_length[i].substring(2, 3);
			}
		}
		else
		{
			v1 = all_lines_length[i].substring(0, 1);
			if (regexp.test(all_lines_length[i].substring(2, 3)))
			{
				v2 = all_lines_length[i].substring(1, 3);
			}
			else
			{
				v2 = all_lines_length[i].substring(1, 2);
			}
		}
		l = +all_lines_length[i].substring(all_lines_length[i].indexOf('=') + 1);
		all_lines_length[i] = {v1: v1, v2: v2, l: l, paint: false};
	}
	console.log(all_lines_length);
	var osnova, b1, b2, line, b1_line, b2_line, ov;
	for (var i = 0; i < all_lines_length.length; i++)
	{
		if (all_lines_length[i].v1 === "A" && all_lines_length[i].v2 === "B" ||
			all_lines_length[i].v1 === "B" && all_lines_length[i].v2 === "A")
		{
			osnova = all_lines_length[i];
			line = new Path.Line(new Point(100, 100), new Point(100 + osnova.l, 100));
			line.strokeColor = 'black';
			line.strokeWidth = 3;
			lines.push(line);
			line.data.id = lines.length - 1;
			all_lines_length[i].paint = true;
			break;
		}
	}
	for (var i = 0; i < all_lines_length.length; i++)
	{
		for (var j = i; j < all_lines_length.length; j++)
		{
			if (all_lines_length[i] !== osnova && all_lines_length[j] !== osnova && i !== j)
			{
				ov = obshaya_vertex(all_lines_length[i], osnova);
				if (ov)
				{
					if (obshaya_vertex(all_lines_length[j], osnova, ov) &&
						obshaya_vertex(all_lines_length[i], all_lines_length[j], ov))
					{
						console.log(all_lines_length[i], all_lines_length[j]);
					}
				}

			}
		}
	}
	console.log(lines);
};

project.activeLayer.applyMatrix = false;
jQuery('#myCanvas').css('resize', 'both');

function resize_canvas()
{
	var nl_value;
	if (window.innerWidth > window.innerHeight) // для мониторов
	{
		jQuery("#myCanvas").css("height", document.body.clientHeight);
		jQuery("#sketch_editor2").css("width", 180);
		jQuery(".div_canvas").css("width", "calc(100% - 190px)");
		jQuery("#myCanvas").css("width", document.body.clientWidth - 200);
		view.viewSize = new Size(document.body.clientWidth - 200, document.body.clientHeight);
		nl_value = elem_newLength.value;
		elem_newLength = document.getElementById('newLength2');
		elem_newLength.value = nl_value;
		elem_useGrid = document.getElementById('useGrid2');
	}
	else //для мобильных
	{
		jQuery(".div_canvas").css("width", "100%");
		jQuery("#myCanvas").css("width", document.body.clientWidth);
		if (elem_window.style.display === "none" || elem_window.style.display === "")
		{
			jQuery("#myCanvas").css("height", document.body.clientHeight - 116);
			view.viewSize = new Size(document.body.clientWidth, document.body.clientHeight - 116);
		}
		else
		{
			jQuery("#myCanvas").css("height", document.body.clientHeight - 216);
			view.viewSize = new Size(document.body.clientWidth, document.body.clientHeight - 216);
		}
		nl_value = elem_newLength.value;
		elem_newLength = document.getElementById('newLength');
		elem_newLength.value = nl_value;
		elem_useGrid = document.getElementById('useGrid');
	}

	view.center = new Point(Math.round(view.viewSize.width / 2), Math.round(view.viewSize.height / 2));

	//sdvig();

	//zoom(1);
}

function go_back()
{
    AndroidFunction.func_back(1);
}

function open_popup()
{
	if (ready)
	{
		jQuery("#popup").css("display", "block");
	}
}

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

var tre = fun_tre.getTre();

var close_sketch_click = function()
{
	if (close_sketch_click_bool || !ready)
	{
		return;
	}

	close_sketch_click_bool = true;

	var l1, l2, lc;
	lines_length = [];
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

    //console.log(lines_length, diag);

	if (tre === 29)
	{
		calc_img = svg_gen(1);
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
	code_og = code;
	alfavit_og = alfavit;

	if (tre === 29)
	{
		if(!tkan())
		{
			if (auto === 0)
			{
				elem_preloader.style.display = 'none';
			}
			else
			{
				window.location = url;
			}
			return;
		}
		cut_img = svg_gen(2);
	}
	else
	{
		g_points = getPathsPointsBySort(lines_sort);
		polotno();

		//var time_end, time_start = performance.now();

		cuts_gen();

		cut_img = svg_gen(2);

		remove_none_shvy();
		rotate_final();
		zerkalo(1);
		remove_pt_intersects();
		calc_img = svg_gen(1);

		if (koordinats_poloten.length > 1)
		{
			seam = 1;
		}

		//time_end = performance.now() - time_start;
		//console.log(time_end, 'images_time');
	}
	//console.log(cuts_json);
	save_data();
	//console.log(p_usadki_final, square_obrezkov, sq_polotna, width_final);
	//console.log(polotna, koordinats_poloten);
	elem_preloader.style.display = 'none';
	close_sketch_click_bool = false;

	AndroidFunction.func_back(1);
};

function save_data(){

    AndroidFunction.func_elem_jform_image(calc_img);
    AndroidFunction.func_elem_jform_image_cut(cut_img);

    var lines_length1 = JSON.stringify(lines_length);
    AndroidFunction.func_elem_jform_ll(lines_length1);

    var koordinats_poloten1 = JSON.stringify (koordinats_poloten);
    AndroidFunction.func_elem_jform_koordinats_poloten(koordinats_poloten1);

    AndroidFunction.func_elem_jform_n4(elem_jform_n4.value);
    AndroidFunction.func_elem_jform_n5(elem_jform_n5.value);
    AndroidFunction.func_elem_jform_n9(elem_jform_n9.value);

    AndroidFunction.func_elem_jform_p_usadki_final(p_usadki_final);

    AndroidFunction.func_elem_jform_square_obrezkov(square_obrezkov);

    js_polotna = JSON.stringify(walls_points);
    AndroidFunction.func_elem_jform_walls_points(js_polotna);
    js_polotna = JSON.stringify(diags_points);
    AndroidFunction.func_elem_jform_diags_points(js_polotna);
    js_polotna = JSON.stringify(pt_points);
    AndroidFunction.func_elem_jform_pt_points(js_polotna);
    AndroidFunction.func_elem_jform_code(code);
    AndroidFunction.func_elem_jform_alfavit(alfavit);

}

function svg_gen(flag)
{
	view.zoom = 1;
	text_points_sdvig();

	if (flag === 1)
	{
		for(var i in text_points)
		{
			text_points[i].fontSize = 14;
			text_points[i].bringToFront();
		}
		for(var i in text_diag)
		{
			text_diag[i].fontSize = 12;
			if (diag_sort[i].length < 40)
			{
				text_diag[i].fontSize -= 1;
				if (diag_sort[i].length < 30)
				{
					text_diag[i].fontSize -= 1;
					if (diag_sort[i].length < 20)
					{
						text_diag[i].fontSize -= 1;
						if (diag_sort[i].length < 10)
						{
							text_diag[i].fontSize -= 1;
						}
					}
				}
			}
			text_diag[i].position = diag_sort[i].position;
			text_diag[i].bringToFront();
		}
		for(var i = lines.length, l_i, tl; i--;)
		{
			l_i = lines[i];
			tl = text_lines[l_i.data.id];
			tl.fontSize = 14;
			if (l_i.length < 40)
			{
				tl.fontSize -= 1;
				if (l_i.length < 30)
				{
					tl.fontSize -= 1;
					if (l_i.length < 20)
					{
						tl.fontSize -= 1;
						if (l_i.length < 10)
						{
							tl.fontSize -= 1;
						}
					}
				}
			}
			tl.position = lines[i].position;
			tl.bringToFront();
		}
	}
	else if (flag === 2)
	{
		var ptfs = 14;
		if (tre === 29)
		{
			p_usadki_final = 1;
			ptfs = 12;
		}

		for(var i in text_points)
		{
			text_points[i].fontSize = ptfs;
			text_points[i].bringToFront();
		}
		for(var i in text_diag)
		{
			text_diag[i].remove();
		}
		for(var i in text_lines)
		{
			text_lines[i].remove();
		}
		for(var i in text_contur)
		{
			text_contur[i].remove();
		}
	}

	view.update();

	var bounds = project.activeLayer.bounds.clone();
	project.activeLayer.bounds = new Rectangle({from: [0,0],
		to: [bounds.bottomRight.x - bounds.topLeft.x,bounds.bottomRight.y - bounds.topLeft.y]});
	var rec = new Rectangle({from: bounds.topLeft, to: bounds.bottomRight});
	var svg = project.activeLayer.exportSVG();
	svg = '<?xml version="1.0" ?><svg height="'+rec.height.toFixed(0)+'px" width="'+rec.width.toFixed(0)+'px" xmlns="http://www.w3.org/2000/svg">'+svg.outerHTML+'</svg>';
	//console.log(svg);
	project.activeLayer.bounds = bounds;
	return svg;
}

function sdvig()
{
	if (chert_close)
	{
		var sx, sy;
		add_granica();

		for (var key = lines.length, l_s0, l_s1; key--;)
		{
			l_s0 = lines[key].segments[0].point;
			l_s1 = lines[key].segments[1].point;
			l_s0.x = l_s0.x.toFixed(2)-0;
			l_s1.x = l_s1.x.toFixed(2)-0;
			l_s0.y = l_s0.y.toFixed(2)-0;
			l_s1.y = l_s1.y.toFixed(2)-0;
		}
		g_points = getPathsPoints(lines);
		var points_m = findMinAndMaxCordinate_g();

		if (points_m.maxY > down_g.position.y)
		{
			sy = -Math.round(Decimal.abs(new Decimal(points_m.maxY).minus(down_g.position.y)).toNumber());
			//('↓');

			sdvig_sy(sy);
		}

		if (points_m.maxX > right_g.position.x)
		{
			sx = -Math.round(Decimal.abs(new Decimal(points_m.maxX).minus(right_g.position.x)).toNumber());
			//('→');

			sdvig_sx(sx);
		}

		if (points_m.minX < left_g.position.x)
		{
			sx = Math.round(Decimal.abs(new Decimal(points_m.minX).minus(left_g.position.x)).toNumber());
			//('←');

			sdvig_sx(sx);
		}

		if (points_m.minY < up_g.position.y)
		{
			sy = Math.round(Decimal.abs(new Decimal(points_m.minY).minus(up_g.position.y)).toNumber());
			//('↑');

			sdvig_sy(sy);
		}
		clear_granica();
		for (var key = lines.length, l_s0, l_s1; key--;)
		{
			l_s0 = lines[key].segments[0].point;
			l_s1 = lines[key].segments[1].point;
			l_s0.x = l_s0.x.toFixed(2)-0;
			l_s1.x = l_s1.x.toFixed(2)-0;
			l_s0.y = l_s0.y.toFixed(2)-0;
			l_s1.y = l_s1.y.toFixed(2)-0;
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
}

function sdvig_sx(sx)
{
	for (var key in project.activeLayer.children)
	{
		project.activeLayer.children[key].position.x = new Decimal(project.activeLayer.children[key].position.x).plus(sx).toNumber();
	}
}

function zoom(flag1)
{
	var z, l = 0;
	for (var key = lines.length; key--;)
	{
		l++;
	}
	if (l === 0)
	{
		return;
	}
	for (var key = lines.length, l_s0, l_s1; key--;)
	{
		l_s0 = lines[key].segments[0].point;
		l_s1 = lines[key].segments[1].point;
		l_s0.x = l_s0.x.toFixed(2)-0;
		l_s1.x = l_s1.x.toFixed(2)-0;
		l_s0.y = l_s0.y.toFixed(2)-0;
		l_s1.y = l_s1.y.toFixed(2)-0;
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
					z = (14 / view.zoom).toFixed(2)-0;
					for (var key = text_points.length; key--;)
					{
						text_points[key].fontSize = z;
					}
					for (var key = text_lines.length; key--;)
					{
						text_lines[key].fontSize = z;
					}
					for (var key = text_diag.length; key--;)
					{
						text_diag[key].fontSize = z;
					}
					for (var key = text_contur.length; key--;)
					{
						text_contur[key].fontSize = z;
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
			if (view.zoom < 2.2)
			{
				view.zoom = new Decimal(view.zoom).plus(0.05).toNumber();
				clear_granica();
				sdvig();
				add_granica();
				z = (14 / view.zoom).toFixed(2)-0;
				for (var key = text_points.length; key--;)
				{
					text_points[key].fontSize = z;
				}
				for (var key = text_lines.length; key--;)
				{
					text_lines[key].fontSize = z;
				}
				for (var key = text_diag.length; key--;)
				{
					text_diag[key].fontSize = z;
				}
				for (var key = text_contur.length; key--;)
				{
					text_contur[key].fontSize = z;
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
	granica = new Path.Rectangle(0, 0, vwz.minus(80 / view.zoom).toNumber(), vhz.minus(80 / view.zoom).toNumber());
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
		for (var key = lines.length; key--;)
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
		for (var key = lines.length; key--;)
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

		move_point.x = view.viewSize.width * (view.zoom - 1) + 20;
		start_point.x += 1;
		begin_point.x += 1;
		end_point.x += 1;
	    break;
	}
	for (var key = lines.length, l_s0, l_s1; key--;)
	{
		l_s0 = lines[key].segments[0].point;
		l_s1 = lines[key].segments[1].point;
		l_s0.x = l_s0.x.toFixed(2)-0;
		l_s1.x = l_s1.x.toFixed(2)-0;
		l_s0.y = l_s0.y.toFixed(2)-0;
		l_s1.y = l_s1.y.toFixed(2)-0;
	}
	start_point.x = start_point.x.toFixed(2)-0;
	start_point.y = start_point.y.toFixed(2)-0;
	end_point.x = end_point.x.toFixed(2)-0;
	end_point.y = end_point.y.toFixed(2)-0;
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
	resize_canvas();
}

tool.onMouseDrag = function(event)
{
	if (move_point === undefined)
	{
		if (fix_point_dvig === undefined)
		{
			return;
		}
		var rast_x = Math.round(fix_point_dvig.x - event.point.x);
		var rast_y = Math.round(fix_point_dvig.y - event.point.y);
		for (var key in project.activeLayer.children)
		{
			project.activeLayer.children[key].position.x -=  rast_x;
			project.activeLayer.children[key].position.y -=  rast_y;
		}

		if (start_point !== undefined && end_point !== undefined)
		{
			start_point.x -= rast_x;
			end_point.x -= rast_x;
			start_point.y -= rast_y;
			end_point.y -= rast_y;
		}
		if (begin_point_diag !== undefined)
		{
			begin_point_diag.x -= rast_x;
			begin_point_diag.y -= rast_y;
		}
		if (end_point_diag !== undefined)
		{
			end_point_diag.x -= rast_x;
			end_point_diag.y -= rast_y;
		}
		fix_point_dvig = event.point;
		peredvig = true;
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

		if (line_v.length < 40 && line_h.length < 40)
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
			for (var key = lines.length; key--;)
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
			for (var key = lines.length; key--;)
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
		for (var key = lines.length; key--;)
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

	for (var key = lines.length; key--;)
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
	if (!peredvig && document.getElementById('popup2').style.display !== "block" && !close_sketch_click_bool)
	{
		var hitResults = project.hitTestAll(event.point, {class: Path, fill: true, stroke: true, tolerance: 3});
		for (var key = hitResults.length; key--;)
		{
			//console.log(hitResults);
			for (var key2 in hitResults)
			{
				if (hitResults[key2].item.segments.length !== 2 && chert_close)
				{
					return;
				}
			}
			if (hitResults[key].item.segments.length === 2)
			{
				if (!triangulate_bool && triangulate_rezhim === 2)
				{
					for (var i = 0; i < diag.length; i++)
					{
						if (diag[i] == hitResults[key].item)
						{
							elem_window.style.display = 'block';
							resize_canvas();
							elem_newLength.focus();
				    		elem_newLength.value = Math.round(diag[i].length);
				    		elem_newLength.select();
				    		first_click = false;
							manual_diag = diag[i];
							manual_diag.strokeColor = 'red';
							diag.splice(i, 1);
							for (var j = text_manual_diags.length; j--;)
							{
								text_manual_diags[j].remove();
							}
							text_manual_diags = [];
							for (var j = diag.length; j--;)
							{
								add_text_unfixed_length_diag(j);
							}
							//console.log(diag);
							save_cancel();
						}
					}
					return;
				}
				if (hitResults[key].item.data.fixed)
				{
					click_on_fixed(hitResults[key].item);
					return;
				}
			}
		}
	}
	peredvig = false;
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

		var l = 0;
		for (var key = lines.length; key--;)
		{
			l++;
		}
		if (l === 0)
		{
			krug_start = new Path.Circle(begin_point, 20);
			krug_start.strokeColor = 'green';
		}

		if (point_ravny(begin_point, start_point) && point_ravny(move_point, end_point)
			|| point_ravny(move_point, start_point) && point_ravny(begin_point, end_point)
			|| line_v.length >= 10 && line_h.length >= 10)
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

		if (line_v.length < 10 && line_h.length < 10 && !good_lines)
		{
			line_v.removeSegments();
			line_v.remove();
			line_h.removeSegments();
			line_h.remove();
			var l = 0;
			for (var key = lines.length; key--;)
			{
				l++;
			}
			if (l === 0)
			{
				krug_start.remove();
			}
			return;
		}

		if (line_v.length >= 10 && !good_lines)
		{
			line_h.removeSegments();
			line_h.remove();
			line_v.strokeColor = 'black';
			move_point = c_point;
			line_pr = line_prodolzhenie(line_v);
			prodolzhit_line(line_pr[0], line_v);
		}
		if (line_h.length >= 10 && !good_lines)
		{
			line_v.removeSegments();
			line_v.remove();
			line_h.strokeColor = 'black';
			var l = 0;
			for (var key = lines.length; key--;)
			{
				l++;
			}
			if (l === 0 && vh === 'v')
			{
				krug_start.remove();
				begin_point = c_point;
				start_point = c_point;
				krug_start = new Path.Circle(begin_point, 20);
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

		var l = 0;
		for (var key = lines.length; key--;)
		{
			l++;
		}
		if (l === 0)
		{
			krug_start = new Path.Circle(begin_point, 20);
			krug_start.strokeColor = 'green';
		}
		if (point_ravny(begin_point, start_point) && point_ravny(move_point, end_point)
			|| point_ravny(move_point, start_point) && point_ravny(begin_point, end_point)
			|| line_v.length >= 10)
		{
			if (line_v.length < 2)
			{
				line_v.removeSegments();
				line_v.remove();

				l = 0;
				for (var key = lines.length; key--;)
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

			l = 0;
			for (var key = lines.length; key--;)
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

	var circle = new Path.Circle(move_point, 20);
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

	lines_sort = [];
	point_start_or_end = undefined;
	if (point_ravny(start_point, end_point))
	{
		krug_end.remove();
		krug_start.remove();
		krug_end = undefined;
		krug_start = undefined;
		var l = 0;
		for (var key = lines.length; key--;)
		{
			l++;
		}
		if (l < 3)
		{
			begin_point = undefined;
			move_point = undefined;
			c_point = undefined;
			return;
		}
		for (var key = lines.length; key--;)
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
	save_cancel();
};


function clicks_pt()
{
	for (var i = text_points.length; i--;)
	{
		if (text_points[i].data.circle !== undefined)
		{
			text_points[i].data.circle.onMouseEnter = function(event)
			{
				if (manual_diag === undefined)
				{
					var l = 0, lf = 0;
					for (var i = lines.length; i--;)
					{
						l++;
						if (lines[i].data.fixed)
						{
							lf++;
						}
					}
					if (l === lf && triangulate_rezhim === 2 && !this.data.selected)
					{
						this.fillColor = 'purple';
						this.data.mouseEnter = true;
					}
				}
			};

			text_points[i].data.circle.onMouseLeave = function(event)
			{
				if (manual_diag === undefined)
				{
					var l = 0, lf = 0;
					for (var i = lines.length; i--;)
					{
						l++;
						if (lines[i].data.fixed)
						{
							lf++;
						}
					}
					if (this.data.mouseEnter === true && !this.data.selected)
					{
						this.fillColor = 'blue';
						this.data.mouseEnter = false;
					}
				}
			};

			text_points[i].data.circle.onMouseDown = function(event)
			{
				if (manual_diag === undefined)
				{
					var l = 0, lf = 0;
					for (var i = lines.length; i--;)
					{
						l++;
						if (lines[i].data.fixed)
						{
							lf++;
						}
					}
					if (l === lf && triangulate_rezhim === 2)
					{
						draw_diag(this);
						return;
					}
				}
			}
		}
	}
}

function draw_diag(cir)
{
	if (begin_point_diag === undefined)
	{
		begin_point_diag = cir.position;
		cir.fillColor = 'Maroon';
		cir.data.selected = true;
	}
	else if (end_point_diag === undefined && !cir.data.selected)
	{
		for (var i = text_points.length; i--;)
		{
			if (text_points[i].data.circle.data.selected)
			{
				text_points[i].data.circle.fillColor = 'blue';
				text_points[i].data.circle.data.selected = false;

				end_point_diag = cir.position;
				var newdiag = new Path.Line(begin_point_diag, end_point_diag);
				newdiag.strokeWidth = 1;
				newdiag.strokeColor = 'black';
				if (good_diag(newdiag))
				{
					elem_window.style.display = 'block';
					resize_canvas();
					elem_newLength.focus();
		    		elem_newLength.value = Math.round(newdiag.length);
		    		elem_newLength.select();
		    		first_click = false;
					manual_diag = newdiag;
					manual_diag.strokeColor = 'red';
					cir.fillColor = 'blue';
					save_cancel();
				}
				else
				{
					newdiag.remove();
				}
				begin_point_diag = undefined;
				end_point_diag = undefined;
				break;
			}
		}
		begin_point_diag = undefined;
		end_point_diag = undefined;
	}
}

function text_points_sdvig()
{
	var angle_line = 0;
	for (var key = lines.length; key--;)
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
			delete lines[line.id];
		}
		line.remove();
		line_pr.removeSegments();
		line_pr.addSegments([p1, p2]);
	}
	else
	{
		lines.push(line);
		line.data.id = lines.length - 1;
	}
}

function draw_lines_text()
{
	for (var key = lines.length; key--;)
	{
		add_text_v_or_h(lines[key]);
	}
}

function add_text_v_or_h(line)
{
	t_l = text_lines[line.data.id];
	if (t_l === undefined)
	{
		text_lines[line.data.id] = new PointText();
		t_l = text_lines[line.data.id];
	}
	project.activeLayer.addChild(t_l);
	t_l.fontFamily = 'arial';
	t_l.fontWeight = 'bold';
	t_l.fillColor = 'black';
	t_l.fontSize = (14 / view.zoom).toFixed(2)-0;
	t_l.content = Math.round(line.length);
	t_l.rotation = 0;
	var angle = (Math.atan((line.segments[1].point.y-line.segments[0].point.y)/(line.segments[1].point.x
		-line.segments[0].point.x))*180)/Math.PI;
    t_l.rotate(angle);
    t_l.position = line.position;
    t_l.bringToFront();
}

function add_text_diag(index)
{
	if (text_diag[index] !== undefined)
	{
		text_diag[index].remove();
	}
	text_diag[index] = new PointText();
	text_diag[index].fontFamily = 'times';
	text_diag[index].fontWeight = 'bold';
	text_diag[index].fontSize = (14 / view.zoom).toFixed(2)-0;
	text_diag[index].content = Math.round(diag_sort[index].length);
	var angle = (Math.atan((diag_sort[index].segments[1].point.y-diag_sort[index].segments[0].point.y)/(diag_sort[index].segments[1].point.x
			-diag_sort[index].segments[0].point.x))*180)/Math.PI;
    text_diag[index].rotate(angle);
    text_diag[index].position = new Point(diag_sort[index].position.x, diag_sort[index].position.y);
    text_diag[index].bringToFront();
}

function add_text_unfixed_length_diag(index)
{
	if (text_manual_diags[index] !== undefined)
	{
		text_manual_diags[index].remove();
	}
	text_manual_diags[index] = new PointText();
	text_manual_diags[index].fontFamily = 'times';
	text_manual_diags[index].fontWeight = 'bold';
	text_manual_diags[index].fontSize = (14 / view.zoom).toFixed(2)-0;
	text_manual_diags[index].content = diag[index].data.unfixed_length;
	var angle = (Math.atan((diag[index].segments[1].point.y-diag[index].segments[0].point.y)/(diag[index].segments[1].point.x
			-diag[index].segments[0].point.x))*180)/Math.PI;
    text_manual_diags[index].rotate(angle);
    text_manual_diags[index].position = new Point(diag[index].position.x, diag[index].position.y);
    text_manual_diags[index].bringToFront();
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
	var hitResults = project.hitTestAll(point, {class: Path, segments: true, tolerance: 22});
	for (var key = hitResults.length; key--;)
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

	for (var key = lines.length; key--;)
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
	if (point1.x > point2.x - 0.5 && point1.x < point2.x + 0.5 && point1.y > point2.y - 0.5 && point1.y < point2.y + 0.5)
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
	var hitResults = project.hitTestAll(point, {class: Path, segments: true, stroke: true, tolerance: 10});
	for (var key = hitResults.length; key--;)
	{
		if (hitResults[key].item.segments.length === 2)
		{
			ret_rez.push(hitResults[key].item);
		}
	}
	return ret_rez;
}

function cancel_last_action()
{
	if (arr_cancel.length > 1)
	{
		arr_cancel.splice(arr_cancel.length - 1);
	}

	project.activeLayer.removeChildren();

	clearInterval(timer_mig);

	var arr_cancel_last_elem = arr_cancel[arr_cancel.length - 1];

	elem_window.style.display = arr_cancel_last_elem.elem_window_style_display;
	resize_canvas();
	elem_newLength.value = arr_cancel_last_elem.elem_newLength_value;
	if (elem_window.style.display === "block")
	{
		first_click = false;
		elem_newLength.focus();
		elem_newLength.select();
	}

	triangulate_bool = arr_cancel_last_elem.triangulate_bool;
	vh = arr_cancel_last_elem.vh;
	chert_close = arr_cancel_last_elem.chert_close;

	view.zoom = arr_cancel_last_elem.zoom;

	code = arr_cancel_last_elem.code;
	alfavit = arr_cancel_last_elem.alfavit;

	ready = arr_cancel_last_elem.ready;
	close_sketch_click_bool = arr_cancel_last_elem.close_sketch_click_bool;

	triangulate_rezhim = arr_cancel_last_elem.triangulate_rezhim;

	manual_diag = undefined;
	if (arr_cancel_last_elem.manual_diag !== undefined)
	{
		manual_diag = arr_cancel_last_elem.manual_diag.clone();
		project.activeLayer.addChild(manual_diag);
	}
	krug_start = undefined;
	if (arr_cancel_last_elem.krug_start !== undefined)
	{
		krug_start = arr_cancel_last_elem.krug_start.clone();
		project.activeLayer.addChild(krug_start);
	}
	krug_end = undefined;
	if (arr_cancel_last_elem.krug_end !== undefined)
	{
		krug_end = arr_cancel_last_elem.krug_end.clone();
		project.activeLayer.addChild(krug_end);
	}

	if (arr_cancel_last_elem.start_point !== undefined)
	{
		start_point = arr_cancel_last_elem.start_point.clone();
	}

	if (arr_cancel_last_elem.end_point !== undefined)
	{
		end_point = arr_cancel_last_elem.end_point.clone();
	}

	lines_sort = [];
	lines = [];
	text_lines = [];

	if (arr_cancel_last_elem.lines_sort !== undefined && arr_cancel_last_elem.lines_sort.length !== 0)
	{
		for (var key = arr_cancel_last_elem.lines_sort.length; key--;)
		{
			lines_sort[key] = arr_cancel_last_elem.lines_sort[key].clone();
			lines[lines_sort[key].data.id] = lines_sort[key];
			if (chert_close)
			{
				add_text_v_or_h(lines_sort[key]);
			}
		}
	}
	else if (arr_cancel_last_elem.lines !== undefined)
	{
		lines = [];
		for (var key in arr_cancel_last_elem.lines)
		{
			lines[arr_cancel_last_elem.lines[key].data.id] = arr_cancel_last_elem.lines[key].clone();
			if (chert_close)
			{
				add_text_v_or_h(lines[lines.length - 1]);
			}
		}
	}

	diag_sort = [];
	diag = [];
	text_diag = [];

	if (triangulate_bool)
	{
		if (arr_cancel_last_elem.diag_sort !== undefined
			&& arr_cancel_last_elem.diag_sort.length !== 0)
		{
			for (var key = arr_cancel_last_elem.diag_sort.length; key--;)
			{
				diag_sort[key] = arr_cancel_last_elem.diag_sort[key].clone();
				diag[key] = diag_sort[key];
			}
		}
		else if (arr_cancel_last_elem.diag !== undefined)
		{
			diag = [];
			for (var key in arr_cancel_last_elem.diag)
			{
				diag[key] = arr_cancel_last_elem.diag[key].clone();
			}
		}

		for (var key in arr_cancel_last_elem.text_diag)
		{
			text_diag[key] = arr_cancel_last_elem.text_diag[key].clone();
		}
	}
	else if (triangulate_rezhim === 2)
	{
		diag = [];
		for (var key in arr_cancel_last_elem.diag)
		{
			diag[key] = arr_cancel_last_elem.diag[key].clone();
		}
		text_manual_diags = [];
		for (var key in arr_cancel_last_elem.text_manual_diags)
		{
			text_manual_diags[key] = arr_cancel_last_elem.text_manual_diags[key].clone();
		}
	}

	text_points = [];
	for (var key in arr_cancel_last_elem.text_points)
	{
		text_points[key] = arr_cancel_last_elem.text_points[key].clone();
		if (arr_cancel_last_elem.text_points[key].data.circle !== undefined)
		{
			text_points[key].data.circle = arr_cancel_last_elem.text_points[key].data.circle.clone();
		}
	}

	g_points = [];
	for (var key in arr_cancel_last_elem.g_points)
	{
		g_points[key] = arr_cancel_last_elem.g_points[key].clone();
	}

	elem_jform_n4.value = arr_cancel_last_elem.elem_jform_n4_value;
	elem_jform_n5.value = arr_cancel_last_elem.elem_jform_n5_value;
	elem_jform_n9.value = arr_cancel_last_elem.elem_jform_n9_value;

	project.activeLayer.addChildren(lines);
	project.activeLayer.addChildren(diag);
	project.activeLayer.addChildren(text_lines);
	project.activeLayer.addChildren(text_diag);
	project.activeLayer.addChildren(text_manual_diags);
	project.activeLayer.addChildren(text_points);
	for (var i = text_points.length; i--;)
	{
		if (text_points[i].data.circle !== undefined)
		{
			project.activeLayer.addChild(text_points[i].data.circle);
		}
	}

	clicks_pt();
	document.getElementById('popup').style.display = 'none';
	document.getElementById('popup2').style.display = 'none';
}

function save_cancel()
{
	project.activeLayer.removeChildren();

	var obj = {};

	obj.elem_window_style_display = elem_window.style.display;
	obj.elem_newLength_value = elem_newLength.value;
	obj.triangulate_rezhim = triangulate_rezhim;

	if (manual_diag !== undefined)
	{
		obj.manual_diag = manual_diag.clone();
		project.activeLayer.addChild(manual_diag);
	}

	if (krug_start !== undefined)
	{
		obj.krug_start = krug_start.clone();
		project.activeLayer.addChild(krug_start);
	}

	if (krug_end !== undefined)
	{
		obj.krug_end = krug_end.clone();
		project.activeLayer.addChild(krug_end);
	}

	if (start_point !== undefined)
	{
		obj.start_point = start_point.clone();
	}

	if (end_point !== undefined)
	{
		obj.end_point = end_point.clone();
	}

	if (lines_sort !== undefined && lines_sort.length !== 0)
	{
		obj.lines_sort = [];
		for (var key = lines_sort.length; key--;)
		{
			obj.lines_sort[key] = lines_sort[key].clone();
		}
	}
	else if (lines !== undefined)
	{
		obj.lines = [];
		for (var key = lines.length; key--;)
		{
			obj.lines[key] = lines[key].clone();
		}
	}

	if (diag_sort !== undefined && diag_sort.length !== 0)
	{
		obj.diag_sort = [];
		for (var key = diag_sort.length; key--;)
		{
			obj.diag_sort[key] = diag_sort[key].clone();
		}
	}
	else if (diag !== undefined)
	{
		obj.diag = [];
		for (var key in diag)
		{
			obj.diag[key] = diag[key].clone();
		}
	}


	obj.text_points = [];
	for (var key in text_points)
	{
		obj.text_points[key] = text_points[key].clone();
		if (text_points[key].data.circle !== undefined)
		{
			obj.text_points[key].data.circle = text_points[key].data.circle.clone();
		}
	}

	obj.text_diag = [];
	for (var key in text_diag)
	{
		obj.text_diag[key] = text_diag[key].clone();
	}

	obj.text_manual_diags = [];
	for (var key in text_manual_diags)
	{
		obj.text_manual_diags[key] = text_manual_diags[key].clone();
	}

	obj.code = code;
	obj.alfavit = alfavit;

	obj.g_points = [];
	for (var key in g_points)
	{
		obj.g_points[key] = g_points[key].clone();
	}

	obj.triangulate_bool = triangulate_bool;
	obj.vh = vh;
	obj.chert_close = chert_close;

	obj.ready = ready;
	obj.close_sketch_click_bool = close_sketch_click_bool;
	obj.elem_jform_n4_value = elem_jform_n4.value;
	obj.elem_jform_n5_value = elem_jform_n5.value;
	obj.elem_jform_n9_value = elem_jform_n9.value;

	obj.zoom = view.zoom;

	arr_cancel.push(obj);

	project.activeLayer.addChildren(lines);
	project.activeLayer.addChildren(diag);
	project.activeLayer.addChildren(text_lines);
	project.activeLayer.addChildren(text_diag);
	project.activeLayer.addChildren(text_manual_diags);
	project.activeLayer.addChildren(text_points);
	for (var i = text_points.length; i--;)
	{
		if (text_points[i].data.circle !== undefined)
		{
			project.activeLayer.addChild(text_points[i].data.circle);
		}
	}
	//console.log(arr_cancel);
}

function clear_elem()
{
	for (var key in project.activeLayer.children)
	{
		project.activeLayer.children[key].remove();
	}
	view.zoom = 1;
	elem_window.style.display = 'none';
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
	text_lines = [];
	code = 64;
	alfavit = 0;
	lines = [];
	lines_sort = [];

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

	manual_diag = undefined;
	for (var key in text_manual_diags)
	{
		text_manual_diags[key].remove();
	}
	text_manual_diags = [];
	triangulate_rezhim = 0;
	elem_jform_n4.value = '';
	elem_jform_n5.value = '';
	elem_jform_n9.value = '';
	ready = false;
	close_sketch_click_bool = false;
	resize_canvas();
}

function resize_wall_begin()
{
    for (var key = 0, l_k; key < lines_sort.length; key++)
    {
    	l_k = lines_sort[key];
    	if (!l_k.data.fixed)
    	{
    		elem_window.style.display = 'block';
    		resize_canvas();
    		elem_newLength.focus();
    		elem_newLength.value = Math.round(l_k.length);
    		elem_newLength.select();
    		first_click = false;
    		l_k.strokeColor = 'red';
    		text_lines[l_k.data.id].fillColor = 'Maroon';
    		timer_mig = setInterval(migalka, 500, l_k);
    		return;
    	}
    }
}

function change_length(line, length, text_point_index)
{
	var rez_lines = line_on_lines(line);
	var oldLength = line.length;
	var Dx, Dy, coef, smez_wall0, smez_wall1, line2, line3, cp1, cp2, point_s, point_sn, point_s2, point_s3, newPoint,
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
	if (!smez_wall0.data.fixed && !smez_wall1.data.fixed)
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
	else if (!smez_wall0.data.fixed)
	{
		line2 = smez_wall0;
		point_s = line.segments[0].point;
		point_sn = line.segments[1].point;
	}
	else if (!smez_wall1.data.fixed)
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

		moveVertexName(line, line2, newPoint);

        line.removeSegments();
	    line.addSegments([cp1, newPoint]);
	    line2.removeSegments();
	    line2.addSegments([newPoint, cp2]);

	    line.data.fixed = true;
	    add_text_v_or_h(line);
	    add_text_v_or_h(line2);

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

		coef = line.data.coef_wall_kos;

		if (coef === undefined)
		{
			line.data.razv_wall_kos = {p1: cp1.clone(), p2: point_s.clone()};
			var chis = new Decimal(new Decimal(line.segments[1].point.y).minus(line.segments[0].point.y)),
				znam = new Decimal(new Decimal(line.segments[1].point.x).minus(line.segments[0].point.x));
	    	coef = chis.dividedBy(znam);
		}

		var dec_length = new Decimal(length);

		Dx = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(coef.pow(2))))).toNumber();
    	Dy = dec_length.times(Decimal.sqrt(new Decimal(1).dividedBy(new Decimal(1).plus(new Decimal(1).dividedBy(coef.pow(2)))))).toNumber();

    	if (line.data.razv_wall_kos.p1.x > line.data.razv_wall_kos.p2.x)
		{
			Dx = new Decimal(-1).times(Dx).toNumber();
		}
		if (line.data.razv_wall_kos.p1.y > line.data.razv_wall_kos.p2.y)
		{
			Dy = new Decimal(-1).times(Dy).toNumber();
		}

    	newPoint = new Point(new Decimal(cp1.x).plus(Dx).toNumber(), new Decimal(cp1.y).plus(Dy).toNumber());

	    if (line_h_or_v(line2) === 'v' && !line3.data.fixed)
	    {
	    	if (line_h_or_v(line3) === null)
	    	{
	    		var chis = new Decimal(new Decimal(line3.segments[1].point.y).minus(line3.segments[0].point.y)),
					znam = new Decimal(new Decimal(line3.segments[1].point.x).minus(line3.segments[0].point.x));
		    	line3.data.coef_wall_kos = chis.dividedBy(znam);
		    	if (point_ravny(cp2, line3.segments[0].point))
		    	{
			    	line3.data.razv_wall_kos = {p1: line3.segments[0].point.clone(), p2: line3.segments[1].point.clone()};
			    }
			    else
			    {
			    	line3.data.razv_wall_kos = {p1: line3.segments[1].point.clone(), p2: line3.segments[0].point.clone()};
			    }
	    	}
	    	cp2 = new Point(newPoint.x, cp2.y);
	    }
	    else if (line_h_or_v(line2) === 'h' && !line3.data.fixed)
	    {
	    	if (line_h_or_v(line3) === null)
	    	{
	    		var chis = new Decimal(new Decimal(line3.segments[1].point.y).minus(line3.segments[0].point.y)),
					znam = new Decimal(new Decimal(line3.segments[1].point.x).minus(line3.segments[0].point.x));
		    	line3.data.coef_wall_kos = chis.dividedBy(znam);
		    	if (point_ravny(cp2, line3.segments[0].point))
		    	{
			    	line3.data.razv_wall_kos = {p1: line3.segments[0].point.clone(), p2: line3.segments[1].point.clone()};
			    }
			    else
			    {
			    	line3.data.razv_wall_kos = {p1: line3.segments[1].point.clone(), p2: line3.segments[0].point.clone()};
			    }
	    	}
	    	cp2 = new Point(cp2.x, newPoint.y);
	    }
	    else
	    {
	    	var chis = new Decimal(new Decimal(line2.segments[1].point.y).minus(line2.segments[0].point.y)),
				znam = new Decimal(new Decimal(line2.segments[1].point.x).minus(line2.segments[0].point.x));
	    	line2.data.coef_wall_kos = chis.dividedBy(znam);
	    	line2.data.razv_wall_kos = {p1: point_s.clone(), p2: cp2.clone()};
	    }

	    if (cp2.x > point_s.x && cp2.x < newPoint.x
    	|| cp2.y > point_s.y && cp2.y < newPoint.y
    	|| cp2.x < point_s.x && cp2.x > newPoint.x
    	|| cp2.y < point_s.y && cp2.y > newPoint.y)
	    {
	    	line2.data.razv_wall = true;
	    }
	    if (cp2.x > point_s3.x && point_s2_old.x < point_s3.x
    	|| cp2.y > point_s3.y && point_s2_old.y < point_s3.y
    	|| cp2.x < point_s3.x && point_s2_old.x > point_s3.x
    	|| cp2.y < point_s3.y && point_s2_old.y > point_s3.y)
	    {
	    	line3.data.razv_wall = true;
	    }

        line.removeSegments();
	    line.addSegments([cp1, newPoint]);
	    line2.removeSegments();
	    line2.addSegments([newPoint, cp2]);

	    line.data.fixed = true;
	    add_text_v_or_h(line);
	    add_text_v_or_h(line2);
	    moveVertexName(line, line2, newPoint);
	    if (line_h_or_v(line2) !== null && !line3.data.fixed)
	    {
	    	line3.removeSegments();
	    	line3.addSegments([cp2, point_s3]);
	    	add_text_v_or_h(line3);
	    	moveVertexName(line2, line3, cp2);
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
	if (line.data.razv_wall)
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
    	line3.data.razv_wall = true;
    }

    line.removeSegments();
	line.addSegments([point_sn, point_s]);
    if (!line3.data.fixed && line_h_or_v(line2) !== null)
    {
	    line2.removeSegments();
	    line2.addSegments([point_s, point_s2]);

	    var chis = new Decimal(new Decimal(line3.segments[1].point.y).minus(line3.segments[0].point.y)),
				znam = new Decimal(new Decimal(line3.segments[1].point.x).minus(line3.segments[0].point.x));
	    line3.data.coef_wall_kos = chis.dividedBy(znam);
	    line3.data.razv_wall_kos = {p1: point_s2_old.clone(), p2: point_s3.clone()};

	    line3.removeSegments();
	    line3.addSegments([point_s2, point_s3]);
	    add_text_v_or_h(line3);
	    moveVertexName(line2, line3, point_s2);
	}
	else
	{
    	if (line2.data.coef_wall_kos === undefined)
    	{
    		var chis = new Decimal(new Decimal(line2.segments[1].point.y).minus(line2.segments[0].point.y)),
				znam = new Decimal(new Decimal(line2.segments[1].point.x).minus(line2.segments[0].point.x));
	    	line2.data.coef_wall_kos = chis.dividedBy(znam);
    		line2.data.razv_wall_kos = {p1: point_s_old.clone(), p2: point_s2_old.clone()};
		}
	    line2.removeSegments();
	    line2.addSegments([point_s, point_s2_old]);
	}
	line.data.fixed = true;
	add_text_v_or_h(line);
	add_text_v_or_h(line2);
	moveVertexName(line, line2, point_s);
}

function triangulator()
{
	for (var i = diag.length; i--;)
	{
		diag[i].remove();
	}
	diag = [];
	diag_sort = [];
	g_points = getPathsPointsBySort(lines_sort);
	//g_points = changePointsOrderForNaming(g_points, 2, lines_sort);
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
	var diag_i_seg0 = diag_i.segments[0].point,
		diag_i_seg1 = diag_i.segments[1].point;
	g_points = getPathsPointsBySort(lines_sort);

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
		for (var key in text_lines)
		{
			text_lines[key].bringToFront();
		}

		for (var key in text_points)
		{
			text_points[key].bringToFront();
			if (text_points[key].data.circle !== undefined)
			{
				text_points[key].data.circle.bringToFront();
			}
		}
		return false;
	}

	chertezh.remove();
	project.activeLayer.addChildren(lines_sort);

	for (var key in text_lines)
	{
		text_lines[key].bringToFront();
	}

	for (var key in text_points)
	{
		text_points[key].bringToFront();
		if (text_points[key].data.circle !== undefined)
		{
			text_points[key].data.circle.bringToFront();
		}
	}

	for (var key = lines_sort.length, ls_k, ls_k_s0, ls_k_s1; key--;)
	{
		ls_k = lines_sort[key];
		ls_k_s0 = ls_k.segments[0].point;
		ls_k_s1 = ls_k.segments[1].point;

		if (isIntersect(diag_i_seg0, diag_i_seg1, ls_k_s0, ls_k_s1))
		{
			return false;
		}
		if (diag_i.contains(ls_k_s0) && diag_i.contains(ls_k_s1))
		{
			return false;
		}
		if (ls_k.contains(diag_i_seg0) && ls_k.contains(diag_i_seg1))
		{
			return false;
		}
		if (point_ravny(ls_k_s0, diag_i_seg0)
			&& point_ravny(ls_k_s1, diag_i_seg1)
			|| point_ravny(ls_k_s0, diag_i_seg1)
			&& point_ravny(ls_k_s1, diag_i_seg0))
		{
			return false;
		}
	}

	for (var key = diag.length, d_k, d_k_s0, d_k_s1; key--;)
	{
		d_k = diag[key];
		d_k_s0 = d_k.segments[0].point;
		d_k_s1 = d_k.segments[1].point;
		if (diag_i === d_k)
		{
			continue;
		}
		if (isIntersect(diag_i_seg0, diag_i_seg1, d_k_s0, d_k_s1))
		{
			return false;
		}
		if (diag_i.contains(d_k_s0) && diag_i.contains(d_k_s1))
		{
			return false;
		}
		if (d_k.contains(diag_i_seg0) && d_k.contains(diag_i_seg1))
		{
			return false;
		}
		if (point_ravny(d_k_s0, diag_i_seg0)
			&& point_ravny(d_k_s1, diag_i_seg1)
			|| point_ravny(d_k_s0, diag_i_seg1)
			&& point_ravny(d_k_s1, diag_i_seg0))
		{
			return false;
		}
	}

	return true;
}


function square()
{
	var chertezh = new Path();
	g_points = getPathsPointsBySort(lines_sort);
	for(var i = g_points.length; i--;)
	{
		chertezh.add(g_points[i]);
	}
	chertezh.closed = true;
	elem_jform_n4.value = Decimal.abs(new Decimal(chertezh.area).dividedBy(10000)).toNumber().toFixed(2);
	chertezh.remove();
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
	for (var i = seam_lines.length; i--;)
	{
		chertezh.addChild(seam_lines[i]);
	}
	chertezh.scale(-1, 1);

	project.activeLayer.removeChildren();

	project.activeLayer.addChildren(lines_sort);
	project.activeLayer.addChildren(diag_sort);
	project.activeLayer.addChildren(text_points);
	for (var i = seam_lines.length; i--;)
	{
		project.activeLayer.addChild(seam_lines[i]);
	}

	chertezh.remove();

	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].segments[0].point.x = lines_sort[i].segments[0].point.x.toFixed(2)-0;
		lines_sort[i].segments[0].point.y = lines_sort[i].segments[0].point.y.toFixed(2)-0;
		lines_sort[i].segments[1].point.x = lines_sort[i].segments[1].point.x.toFixed(2)-0;
		lines_sort[i].segments[1].point.y = lines_sort[i].segments[1].point.y.toFixed(2)-0;
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].segments[0].point.x = diag_sort[i].segments[0].point.x.toFixed(2)-0;
		diag_sort[i].segments[0].point.y = diag_sort[i].segments[0].point.y.toFixed(2)-0;
		diag_sort[i].segments[1].point.x = diag_sort[i].segments[1].point.x.toFixed(2)-0;
		diag_sort[i].segments[1].point.y = diag_sort[i].segments[1].point.y.toFixed(2)-0;
	}
	okruglenie_all_segments();

	for (var key = 0, l_k; key < lines_sort.length; key++)
    {
    	l_k = lines_sort[key];
    	add_text_v_or_h(l_k);
    	if (p_u === 1)
    	{
    		text_lines[l_k.data.id].content = (+text_lines[l_k.data.id].content);
    	}
    	else
    	{
	    	text_lines[l_k.data.id].content = (+text_lines[l_k.data.id].content * p_u).toFixed(1);
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
	var line;
	seam_lines = [];
	for (var i = lines_sort.length; i--;)
	{
		lines_sort[i].segments[0].point.x = lines_sort[i].segments[0].point.x.toFixed(2)-0;
		lines_sort[i].segments[0].point.y = lines_sort[i].segments[0].point.y.toFixed(2)-0;
		lines_sort[i].segments[1].point.x = lines_sort[i].segments[1].point.x.toFixed(2)-0;
		lines_sort[i].segments[1].point.y = lines_sort[i].segments[1].point.y.toFixed(2)-0;
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].segments[0].point.x = diag_sort[i].segments[0].point.x.toFixed(2)-0;
		diag_sort[i].segments[0].point.y = diag_sort[i].segments[0].point.y.toFixed(2)-0;
		diag_sort[i].segments[1].point.x = diag_sort[i].segments[1].point.x.toFixed(2)-0;
		diag_sort[i].segments[1].point.y = diag_sort[i].segments[1].point.y.toFixed(2)-0;
	}
	okruglenie_all_segments();
	var points_m = findMinAndMaxCordinate_g();

	for (var i = polotna.length; i--;)
	{
		if (i === 0 )
		{
			break;
		}
		line = new Path.Line(polotna[i].polotno.bounds.bottomLeft.clone(), polotna[i].polotno.bounds.bottomRight.clone());
		project.activeLayer.addChild(line);
		line.strokeWidth = 2;
		line.strokeColor = 'red';
		polotna[i].polotno.remove();
		polotna[i].parts.remove();
		polotna[i].cuts.remove();
		seam_lines.push(line);
	}

    AndroidFunction.func_elem_seam(seam_lines.push(line));
}

function rotate_final()
{
	for (var i = project.activeLayer.children.length; i--;)
	{
		if (project.activeLayer.children[i].segments !== undefined)
		{
			project.activeLayer.children[i].rotate(angle_final, view.center);
		}
	}
	okruglenie_all_segments();
}

function remove_pt_intersects()
{
	var del_bool = true;
	for (var i = text_points.length; i--;)
	{
		if (text_points[i].data.id_line1 === undefined || text_points[i].data.id_line2 === undefined)
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

		lines_sort[i].segments[0].point.x = lines_sort[i].segments[0].point.x.toFixed(2)-0;
		lines_sort[i].segments[0].point.y = lines_sort[i].segments[0].point.y.toFixed(2)-0;
		lines_sort[i].segments[1].point.x = lines_sort[i].segments[1].point.x.toFixed(2)-0;
		lines_sort[i].segments[1].point.y = lines_sort[i].segments[1].point.y.toFixed(2)-0;
	}
	for (var i = diag_sort.length; i--;)
	{
		diag_sort[i].rotate(gradus_f, view.center);

		diag_sort[i].segments[0].point.x = diag_sort[i].segments[0].point.x.toFixed(2)-0;
		diag_sort[i].segments[0].point.y = diag_sort[i].segments[0].point.y.toFixed(2)-0;
		diag_sort[i].segments[1].point.x = diag_sort[i].segments[1].point.x.toFixed(2)-0;
		diag_sort[i].segments[1].point.y = diag_sort[i].segments[1].point.y.toFixed(2)-0;
	}
	okruglenie_all_segments();
	angle_final = -gradus_f;

	add_polotno(width_polotna[j_f].width, p_usadki, kolvo_poloten);

	for (var key = lines_sort.length, l_k; key--;)
    {
    	l_k = lines_sort[key];
    	add_text_v_or_h(l_k);
    	text_lines[l_k.data.id].content = (+text_lines[l_k.data.id].content * p_usadki).toFixed(1);
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

	var sq_obr = 0, sq_polo = 0;
	for (var key = polotna.length; key--;)
	{
		polotna[key].polotno.strokeColor = 'red';
		polotna[key].polotno.fillColor = null;
		polotna[key].polotno.dashArray = [10, 4];
		polotna[key].polotno.opacity = 1;
		polotna[key].polotno.selected = false;

		polotna[key].parts.strokeColor = null;
		polotna[key].parts.fillColor = 'green';
		polotna[key].parts.dashArray = [];
		polotna[key].parts.opacity = 0.4;
		polotna[key].parts.selected = true;

		polotna[key].cuts.strokeColor = null;
		polotna[key].cuts.fillColor = 'red';
		polotna[key].cuts.dashArray = [];
		polotna[key].cuts.opacity = 0.3;
		polotna[key].cuts.selected = false;


		if (polotna[key].cuts.children !== undefined)
		{
			for (var key2 = polotna[key].cuts.children.length; key2--;)
			{
				sq_obr = new Decimal(Math.abs(polotna[key].cuts.children[key2].area)).plus(sq_obr).toNumber();
			}
		}
		else
		{
			sq_obr = new Decimal(Math.abs(polotna[key].cuts.area)).plus(sq_obr).toNumber();
		}

		sq_polo = new Decimal(Math.abs(polotna[key].polotno.area)).plus(sq_polo).toNumber();
	}

	sq_obr = new Decimal(sq_obr).times(p_usadki).times(p_usadki).toNumber();
	sq_polo = new Decimal(sq_polo).times(p_usadki).times(p_usadki).toNumber();
	sq_obr = new Decimal(sq_obr).dividedBy(10000).toNumber();
	sq_polo = new Decimal(sq_polo).dividedBy(10000).toNumber();

	width_final = width_polotna[j_f].width;

    AndroidFunction.func_elem_jform_width(width_final);

	sq_polotna = sq_polo;
	p_usadki_final = p_usadki;
	square_obrezkov = sq_obr;
}

var polotno = function()
{
	var gradus_f, j_f, break_bool = false, points_m, kolvo_poloten = 1, height_chert, j_n, p_usadki, h, hp;
	var sq_og = elem_jform_n4.value-0;
	var price_it, sq_obr, sq_min, price_min, sq1, sq1_dec, usadka_final;
	//var time_end, time_start = performance.now();
	var chertezh;

	while (true)
	{
		sq_min = 100222;
		price_min = 10222444;
		usadka_final = 0;

		for (var angle_rotate = 0; angle_rotate < 360; angle_rotate++)
		{
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

				//console.log(kolvo_poloten, width_polotna[j].width, angle_rotate);
				if (add_polotno(width_polotna[j].width, p_usadki, kolvo_poloten) === false)
				{
					continue;
				}

				if (kolvo_polos === kolvo_poloten)
				{
					sq_obr = 0;
					for (var key = polotna.length; key--;)
					{
						if (polotna[key].cuts.children !== undefined)
						{
							for (var key2 = polotna[key].cuts.children.length; key2--;)
							{
								sq_obr = new Decimal(Math.abs(polotna[key].cuts.children[key2].area)).plus(sq_obr).toNumber();
							}
						}
						else
						{
							sq_obr = new Decimal(Math.abs(polotna[key].cuts.area)).plus(sq_obr).toNumber();
						}
					}

					if ((width_polotna[j].price < price_min) || (width_polotna[j].price === price_min && sq_obr < sq_min))
					{
						price_min = width_polotna[j].price;
						sq_min = sq_obr;
						gradus_f = angle_rotate;
						j_f = j;
						usadka_final = p_usadki;
						break_bool = true;
						//console.log(kolvo_poloten, price_min, sq_min, usadka_final, width_polotna[j].width, angle_rotate);
					}

					if (polotna[polotna.length - 1].polotno.bounds.top <= points_m.minY)
					{
						break;
					}
				}
			}

			for (var i = lines_sort.length; i--;)
			{
				lines_sort[i].rotate(1, view.center);
				//lines_sort[i].segments[0].point.x = +lines_sort[i].segments[0].point.x.toFixed(2);
				//lines_sort[i].segments[0].point.y = +lines_sort[i].segments[0].point.y.toFixed(2);
				//lines_sort[i].segments[1].point.x = +lines_sort[i].segments[1].point.x.toFixed(2);
				//lines_sort[i].segments[1].point.y = +lines_sort[i].segments[1].point.y.toFixed(2);
			}
			for (var i = diag_sort.length; i--;)
			{
				diag_sort[i].rotate(1, view.center);
				//diag_sort[i].segments[0].point.x = +diag_sort[i].segments[0].point.x.toFixed(2);
				//diag_sort[i].segments[0].point.y = +diag_sort[i].segments[0].point.y.toFixed(2);
				//diag_sort[i].segments[1].point.x = +diag_sort[i].segments[1].point.x.toFixed(2);
				//diag_sort[i].segments[1].point.y = +diag_sort[i].segments[1].point.y.toFixed(2);
			}
			okruglenie_all_segments();
		}

		if (break_bool)
		{
			break;
		}
		kolvo_poloten++;
	}

	okruglenie_all_segments();
	polotno_final(gradus_f, j_f, kolvo_poloten, usadka_final);

	//time_end = performance.now() - time_start;
	//console.log(time_end, 'raskroy_time');
};

function add_polotno(p_width, p_usadki, kolvo_poloten)
{
	for (var key = polotna.length; key--;)
	{
		polotna[key].parts.remove();
		polotna[key].polotno.remove();
		polotna[key].cuts.remove();
	}
	polotna = [];
	kolvo_polos = 0;
	//var dotyanem = new Decimal(p_width).times(0.05).toNumber();
	p_width = new Decimal(p_width).dividedBy(p_usadki).toNumber();

	var points_m = findMinAndMaxCordinate_g();
	var down_y = points_m.maxY;
	var up_y = down_y;
	var polotno, parts, rect, obj, cuts, rolik;
	var obj_polotno, obj_parts, obj_cuts;

	var chertezh = new Path();
	for (var key = g_points.length; key--;)
	{
		chertezh.add(g_points[key]);
	}

	chertezh.closed = true;

	while (up_y > points_m.minY)
	{
		kolvo_polos++;
		if (kolvo_polos > kolvo_poloten)
		{
			return false;
		}
		up_y = new Decimal(down_y).minus(p_width).toNumber();
		rolik = new Path();
		rolik.add(new Point(points_m.minX, down_y));
		rolik.add(new Point(points_m.minX, up_y));
		rolik.add(new Point(points_m.maxX, up_y));
		rolik.add(new Point(points_m.maxX, down_y));
		rolik.closed = true;

		parts = chertezh.intersect(rolik);

		rolik.remove();

		if (parts.children === undefined)
		{
			rect = parts.bounds;
			polotno = new Path({
				segments: [	new Point(rect.left + 1, down_y),
							new Point(rect.left + 1, up_y),
							new Point(rect.right - 1, up_y),
							new Point(rect.right - 1, down_y)],
			    closed: true
			});
			cuts = polotno.exclude(parts);
			if (cuts.children !== undefined)
			{
				for (var j = cuts.children.length; j--;)
				{
					//if (Math.abs(cuts.children[j].area) < 1000)
					if (cuts.children[j].area < 100)
					{
						cuts.children[j].remove();
					}
				}
			}
			obj = {parts: parts, polotno: polotno, cuts: cuts};
			polotna.push(obj);
		}
		else
		{
			for (var i = parts.children.length; i--;)
			{
				rect = parts.children[i].bounds;
				polotno = new Path({
					segments: [	new Point(rect.left + 1, down_y),
								new Point(rect.left + 1, up_y),
								new Point(rect.right - 1, up_y),
								new Point(rect.right - 1, down_y)],
				    closed: true
				});
				cuts = polotno.exclude(parts.children[i]);
				if (cuts.children !== undefined)
				{
					for (var j = cuts.children.length; j--;)
					{
						//if (Math.abs(cuts.children[j].area) < 1000)
						if (cuts.children[j].area < 100)
						{
							cuts.children[j].remove();
						}
					}
				}
				obj_parts = parts.children[i];
				obj_parts.remove();
				project.activeLayer.addChild(obj_parts);
				obj = {parts: obj_parts, polotno: polotno, cuts: cuts};
				polotna.push(obj);
			}
		}

		down_y = up_y;
	}

	for (var i = polotna.length, p_i; i--;)
	{
		p_i = polotna[i];
		for (var j = polotna.length, p_j; j--;)
		{
			p_j = polotna[j];
			if (p_i != polotna[j] &&
				p_i.polotno.position.y + 1 > p_j.polotno.position.y &&
				p_i.polotno.position.y - 1 < p_j.polotno.position.y &&
				p_i.polotno.intersects(p_j.polotno))
			{
				obj_parts = p_i.parts.unite(p_j.parts);
				obj_polotno = p_i.polotno.unite(p_j.polotno);
				obj_cuts = obj_polotno.exclude(obj_parts);

				p_i.parts.remove();
				p_i.cuts.remove();
				p_i.polotno.remove();

				p_j.parts.remove();
				p_j.cuts.remove();
				p_j.polotno.remove();

				p_i.parts = obj_parts;
				p_i.cuts = obj_cuts;
				p_i.polotno = obj_polotno;

				polotna.splice(j, 1);
				if (j < i)
				{
					i--;
				}
			}
		}
	}

	chertezh.remove();
	return true;
}

function get_koordinats_poloten(p_usadki)
{
	koordinats_poloten = [];
	for (var i = 0; i < polotna.length; i++)
	{
		koordinats_poloten[i] = [];
		if (polotna[i].parts.children !== undefined)
		{
			for (var j = polotna[i].parts.children.length; j--;)
			{
				for (var k = polotna[i].parts.children[j].segments.length; k--;)
				{
					push_koordinata(polotna[i].parts.children[j].segments[k].point, i, p_usadki);
				}
			}
		}
		else
		{
			for (var k = polotna[i].parts.segments.length; k--;)
			{
				push_koordinata(polotna[i].parts.segments[k].point, i, p_usadki);
			}
		}
	}
	koordinats_poloten = sort_koordinats(koordinats_poloten);
}

function push_koordinata(point, num_polo, p_usadki)
{
	var name, kx, ky, pt, break_bool = false;
	for (var l = text_points.length; l--;)
	{
		if (point_ravny(new Point(point.x - 10, point.y - 5), text_points[l].point))
		{
			name = text_points[l].content;
			break_bool = true;
			break;
		}
	}
	if (!break_bool)
	{
		if (code === 90)
    	{
    		code = 64;
    		alfavit++;
    	}
    	code++;
    	if (alfavit === 0)
    	{
            name = String.fromCharCode(code);
        }
        else
        {
        	name = String.fromCharCode(code) + alfavit;
        }
		pt = new PointText(
        {
            point: new Point(point.x - 10, point.y - 5),
            content: name,
            fillColor: 'blue',
            justification: 'center',
            fontFamily: 'lucida console',
            fontWeight: 'bold',
            fontSize: text_points[0].fontSize
        });
    	text_points.push(pt);
	}
	kx = new Decimal(point.x).minus(polotna[num_polo].polotno.bounds.left).toNumber();
	ky = new Decimal(polotna[num_polo].polotno.bounds.bottom).minus(point.y).toNumber();
	kx = new Decimal(kx).times(p_usadki).toNumber();
	ky = new Decimal(ky).times(p_usadki).toNumber();
	if (kx < 0)
	{
		kx = 0;
	}
	if (ky < 0)
	{
		ky = 0;
	}
	kx = kx.toFixed(1)-0;
	ky = ky.toFixed(1)-0;
	koordinats_poloten[num_polo].push({name: name, koordinats: "(" + kx + "; " + ky + ")"});
}

function sort_koordinats(koordinats_poloten)
{
	for (var i = koordinats_poloten.length; i--;)
	{
		koordinats_poloten[i] = quicksort(koordinats_poloten[i], 0, koordinats_poloten[i].length - 1);
	}
	return koordinats_poloten;
}

function quicksort(a, lo, hi)
{
    if (lo < hi)
    {
        p = partition(a, lo, hi);
        a = quicksort(a, lo, p - 1);
        a = quicksort(a, p + 1, hi);
    }
    return a;
}

function partition(a, lo, hi)
{
    var pivot = a[hi];
    var i = lo;
    var ob;

    for (j = lo; j < hi; j++)
    {
        if (a[j].name <= pivot.name)
        {
            ob = a[i];
            a[i] = a[j];
            a[j] = ob;
            i = i + 1;
        }
    }
    ob = a[i];
    a[i] = a[hi];
    a[hi] = ob;
    return i;
}

function obshaya_point(line1, line2)
{
	var l1s = line1.segments[0].point,
		l2s0 = line2.segments[0].point,
		l2s1 = line2.segments[1].point;
	if (point_ravny(l1s, l2s0)
		|| point_ravny(l1s, l2s1))
	{
		return l1s.clone();
	}
	l1s = line1.segments[1].point;
	if (point_ravny(l1s, l2s0)
		|| point_ravny(l1s, l2s1))
	{
		return l1s.clone();
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
	var Dx, Dy;

	var pd0 = diag_sort[index].segments[0].point.clone();
	var pd1 = diag_sort[index].segments[1].point.clone();
	var a1,b1,a2,b2,c,o,intersections;
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

    a1.removeSegments();
    a1.addSegments([pd1, newObshayaPoint1]);
    b1.removeSegments();
    b1.addSegments([newObshayaPoint1, newPoint]);
    add_text_v_or_h(a1);
    add_text_v_or_h(b1);

    moveVertexName(a1, b1, newObshayaPoint1);

    a2.removeSegments();
    a2.addSegments([pd1, newObshayaPoint2]);
    b2.removeSegments();
    b2.addSegments([newObshayaPoint2, newPoint]);
    add_text_v_or_h(a2);
    add_text_v_or_h(b2);

    moveVertexName(a2, b2, newObshayaPoint2);

	c.removeSegments();
    c.addSegments([newPoint, pd1]);
    add_text_diag(index);
    moveVertexName(b1, b2, newPoint);
   	diag_sort[index].data.fixed = true;
}

function moveVertexName(line1, line2, newPoint)
{
	var pt_id;
	for (var key in text_points)
	{
		if (text_points[key].data.id_line1 === line1.data.id && text_points[key].data.id_line2 === line2.data.id
			|| text_points[key].data.id_line2 === line1.data.id && text_points[key].data.id_line1 === line2.data.id)
		{
			text_points[key].point = new Point(newPoint.x - 10, newPoint.y - 5);
			break;
		}
	}
}

function click_on_fixed(line)
{
	line.data.fixed = false;
	clearInterval(timer_mig);
	for (var key = 0, l_k; key < lines_sort.length; key++)
    {
    	l_k = lines_sort[key];
    	if (!l_k.data.fixed)
    	{
    		l_k.strokeColor = 'black';
    		text_lines[l_k.data.id].fillColor = 'black';
    	}
    }

    for (var key = 0; key < diag_sort.length; key++)
    {
    	if (!diag_sort[key].data.fixed)
    	{
    		diag_sort[key].strokeColor = 'black';
    		text_diag[key].fillColor = 'black';
    	}
    }

    ready = false;
    elem_window.style.display = 'block';
    resize_canvas();

    if (line.strokeWidth === 3)
    {
		triangulate_bool = false;

	    for (var key = 0; key < lines_sort.length, l_k; key++)
	    {
	    	l_k = lines_sort[key];
	    	if (!l_k.data.fixed)
	    	{
	    		elem_preloader.style.display = 'none';
	    		l_k.strokeColor = 'red';
	    		text_lines[l_k.data.id].fillColor = 'Maroon';
	    		timer_mig = setInterval(migalka, 500, l_k);
	    		elem_newLength.focus();
	    		elem_newLength.value = Math.round(l_k.length);
	    		elem_newLength.select();
	    		first_click = false;
	    		save_cancel();
	    		return;
	    	}
	    }
	}
	else
	{
		for (var key = 0; key < diag_sort.length; key++)
	    {
	    	if (!diag_sort[key].data.fixed)
	    	{
	    		elem_preloader.style.display = 'none';
	    		diag_sort[key].strokeColor = 'red';
	    		text_diag[key].fillColor = 'Maroon';
	    		timer_mig = setInterval(migalka, 500, diag_sort[key]);
	    		elem_newLength.focus();
	    		elem_newLength.value = Math.round(diag_sort[key].length);
	    		elem_newLength.select();
	    		first_click = false;
	    		save_cancel();
	    		return;
	    	}
	    }
	}
}

var change_length_all_diags = function()
{
	try
	{
		for (var key = text_manual_diags.length; key--;)
		{
			text_manual_diags[key].remove();
			text_manual_diags[key] = undefined;
		}
		text_manual_diags = [];
		diag_sortirovka();
		triangulate_bool = true;
		for (var key = text_points.length; key--;)
		{
			text_points[key].data.circle.remove();
			text_points[key].data.circle = undefined;
		}
		for (var key = 0; key < diag_sort.length; key++)
		{
    		if (lines.length > 4)
    		{
	    		change_length_diag(key, diag_sort[key].data.unfixed_length, false);
	    	}
	    	else
	    	{
	    		change_length_diag_4angle(key, diag_sort[key].data.unfixed_length, false);
	    	}
		}
		triangulate_rezhim = 0;
		square();
		text_points_sdvig();
		ready = true;
		sdvig();
		zoom(1);
		elem_preloader.style.display = 'none';
	}
	catch(e)
	{
		elem_preloader.style.display = 'none';
		cancel_last_action();
		alert('Ошибка!');
	}
};

var ok_enter_process, triangulate_bool = false, change_length_all_diags_process;

function ok_enter_all()
{
	if (manual_diag !== undefined)
	{
		var regexp = /^\d+$/;
		if (regexp.test(elem_newLength.value))
		{
			if (elem_newLength.value < 3)
			{
				alert('Слишком маленькая длина!');
				elem_newLength.focus();
	    		elem_newLength.value = Math.round(manual_diag.length);
	    		elem_newLength.select();
				return;
			}
			if (elem_newLength.value > 10000)
			{
				alert('Слишком большая длина!');
				elem_newLength.focus();
	    		elem_newLength.value = Math.round(manual_diag.length);
	    		elem_newLength.select();
				return;
			}
		}
		else
		{
			alert('Недопустимые символы!');
			elem_newLength.focus();
    		elem_newLength.value = Math.round(manual_diag.length);
    		elem_newLength.select();
			return;
		}
		manual_diag.data.unfixed_length = +elem_newLength.value;
		manual_diag.strokeColor = 'green';
		diag.push(manual_diag);
		add_text_unfixed_length_diag(diag.length - 1);
		manual_diag = undefined;
		elem_window.style.display = 'none';
		resize_canvas();
		if (diag.length === lines.length - 3)
		{
			elem_preloader.style.display = 'block';
			change_length_all_diags_process = change_length_all_diags.process();
			setTimeout(change_length_all_diags_process, 200);
		}
		save_cancel();
		return;
	}
	var kol_fix = 0;
	for (var key = lines_sort.length; key--;)
	{
		if (lines_sort[key].data.fixed)
		{
			kol_fix++;
		}
	}
	if ((kol_fix === lines_sort.length || kol_fix === lines_sort.length - 1) &&
		!triangulate_bool && (triangulate_rezhim === 1 || triangulator_pro === 0))
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
				if (!diag_sort[key].data.fixed)
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
		    	if (diag_sort[key].data.fixed)
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
		    		save_cancel();
		    		return;
		    	}
		    }
		    square();
		    elem_window.style.display = 'none';
		    resize_canvas();
		    text_points_sdvig();

		    ready = true;
		    save_cancel();
		}
		else
		{
			for (var i = lines_sort.length; i--;)
			{
				lines_sort[i].segments[0].point.x = lines_sort[i].segments[0].point.x.toFixed(2)-0;
				lines_sort[i].segments[0].point.y = lines_sort[i].segments[0].point.y.toFixed(2)-0;
				lines_sort[i].segments[1].point.x = lines_sort[i].segments[1].point.x.toFixed(2)-0;
				lines_sort[i].segments[1].point.y = lines_sort[i].segments[1].point.y.toFixed(2)-0;
			}
			okruglenie_all_segments();
			for (var key = 0; key < lines_sort.length; key++)
		    {
		    	if (!lines_sort[key].data.fixed)
		    	{
		    		change_length(lines_sort[key], str_length, key);
		    		sdvig();
	    			zoom(1);
		    		break;
		    	}
		    }
		    for (var key = 0, l_k; key < lines_sort.length; key++)
		    {
		    	l_k = lines_sort[key];
		    	if (l_k.data.fixed)
		    	{
		    		l_k.strokeColor = 'green';
		    	}
		    	else
		    	{
		    		elem_preloader.style.display = 'none';
		    		l_k.strokeColor = 'red';
		    		text_lines[l_k.data.id].fillColor = 'Maroon';
		    		timer_mig = setInterval(migalka, 500, l_k);
		    		elem_newLength.focus();
		    		elem_newLength.value = Math.round(l_k.length);
		    		elem_newLength.select();
		    		first_click = false;
		    		save_cancel();
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

			if (triangulator_pro === 1 && triangulate_rezhim === 0)
			{
				for (var i = diag.length; i--;)
				{
					diag[i].remove();
					if (text_diag[i] !== undefined)
					{
						text_diag[i].remove();
					}
				}
				diag = [];
				diag_sort = [];
				text_diag = [];
				document.getElementById('popup2').style.display = 'block';
				elem_window.style.display = 'none';
				resize_canvas();
				return;
			}
			else if (triangulate_rezhim === 1 || triangulator_pro === 0)
			{
				l = 0;
				l1 = 0;
				for (var key = lines.length; key--;)
				{
					l1++;
					if (lines[key].data.fixed)
					{
						l++;
					}
				}

				if(l1 === l)
				{
					for (var key = lines_sort.length; key--;)
					{
						lines_sort[key].segments[0].point.x = lines_sort[key].segments[0].point.x.toFixed(2)-0;
						lines_sort[key].segments[0].point.y = lines_sort[key].segments[0].point.y.toFixed(2)-0;
						lines_sort[key].segments[1].point.x = lines_sort[key].segments[1].point.x.toFixed(2)-0;
						lines_sort[key].segments[1].point.y = lines_sort[key].segments[1].point.y.toFixed(2)-0;
					}
					triangulator();
					if (diag.length !== k - 3)
					{
						for (var key = 3; key--;)
						{
							pulemet();
							//console.log('pulemet');
							if (diag.length === k - 3)
							{
								break;
							}
						}
					}
					if (diag.length !== k - 3)
					{
						alert('Ошибка в построении диагоналей!');
						//console.log(diag);
						//console.log(diag_sort);
						elem_preloader.style.display = 'none';
						cancel_last_action();
						return;
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
					lines_sort[i].segments[0].point.x = lines_sort[i].segments[0].point.x.toFixed(2)-0;
					lines_sort[i].segments[0].point.y = lines_sort[i].segments[0].point.y.toFixed(2)-0;
					lines_sort[i].segments[1].point.x = lines_sort[i].segments[1].point.x.toFixed(2)-0;
					lines_sort[i].segments[1].point.y = lines_sort[i].segments[1].point.y.toFixed(2)-0;
				}
				for (var i = diag_sort.length; i--;)
				{
					diag_sort[i].segments[0].point.x = diag_sort[i].segments[0].point.x.toFixed(2)-0;
					diag_sort[i].segments[0].point.y = diag_sort[i].segments[0].point.y.toFixed(2)-0;
					diag_sort[i].segments[1].point.x = diag_sort[i].segments[1].point.x.toFixed(2)-0;
					diag_sort[i].segments[1].point.y = diag_sort[i].segments[1].point.y.toFixed(2)-0;
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
					resize_canvas();
					text_points_sdvig();

					ready = true;
		    	}
		    	save_cancel();
		    }
		    else
		    {
		    	elem_window.style.display = 'none';
		    	resize_canvas();
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
	var d_c_w = [], d_c = [];
	for (var i = diag.length; i--;)
	{
		d_c_w[i] = diag_count_walls(diag[i]);
		d_c[i] = diag_count(diag[i]);
		diag[i].data.d_c_w = d_c_w[i];
		diag[i].data.d_c = d_c[i];
	}
	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 4)
		{
			diag_sort.push(diag[i]);
		}
	}
	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 3)
		{
			diag_sort.push(diag[i]);
		}
	}
	for (var i = diag.length; i--;)
	{
		hitResults0 = project.hitTestAll(diag[i].segments[0].point, {class: Path, segments: true, tolerance: 2});
		hitResults1 = project.hitTestAll(diag[i].segments[1].point, {class: Path, segments: true, tolerance: 2});
		if (d_c_w[i] === 2 && d_c[i] >= 2 && (hitResults0.length === 3 || hitResults1.length === 3))
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

	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 2 && d_c[i] >= 2)
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
	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 2 && d_c[i] >= 1)
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
	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 2 && d_c[i] >= 0)
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

	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 1)
		{
			diag_sort.push(diag[i]);
		}
	}
	for (var i = diag.length; i--;)
	{
		if (d_c_w[i] === 0)
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

function diag_count(diag)
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
	var Dx, Dy, coef;
	var pd0, pd1, pd2;
	var a1,b1,a2,b2,o,intersections, op;
	var line_arr = [], points_m, c = diag_sort[index], c_s0 = c.segments[0].point, c_s1 = c.segments[1].point;
	var newPoint,newObshayaPoint1,newObshayaPoint2,oldObshayaPoint1,oldObshayaPoint2;
	var angle_new1, angle_old1, angle_rotate1, angle_new2, angle_old2, angle_rotate2,
	angle_new3, angle_old3, angle_rotate3, angle_new4, angle_old4, angle_rotate4, angle_new, angle_old, angle_rotate;
	var hitResults0 = project.hitTestAll(c_s0, {class: Path, segments: true, tolerance: 2});
	var hitResults1 = project.hitTestAll(c_s1, {class: Path, segments: true, tolerance: 2});

	if (c.data.d_c_w === 3)
	{
		hitResults0 = project.hitTestAll(c_s0, {class: Path, segments: true, tolerance: 2});
		if (hitResults0.length === 3)
		{
			pd0 = c_s0.clone();
			pd1 = c_s1.clone();
		}
		else
		{
			pd0 = c_s1.clone();
			pd1 = c_s0.clone();
		}

		for (var i = lines.length, l_i, l_i_s0, l_i_s1; i--;)
		{
			l_i = lines[i];
			l_i_s0 = l_i.segments[0].point;
			l_i_s1 = l_i.segments[1].point;
			for (var j = lines.length, l_j; j--;)
			{
				l_j = lines[j];
				l_j_s0 = l_j.segments[0].point;
				l_j_s1 = l_j.segments[1].point;
				if (obshaya_point(l_i, l_j) !== null && l_i !== l_j)
				{
					if (!point_ravny(obshaya_point(l_i, l_j), pd0) && !point_ravny(obshaya_point(l_i, l_j), pd1))
					{
						if ((point_ravny(l_i_s0, pd0) || point_ravny(l_i_s1, pd0)
							|| point_ravny(l_i_s0, pd1) || point_ravny(l_i_s1, pd1))
							&& (point_ravny(l_j_s0, pd0) || point_ravny(l_j_s1, pd0)
							|| point_ravny(l_j_s0, pd1) || point_ravny(l_j_s1, pd1)))
						{
							a1 = l_i;
							b1 = l_j;
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

		for (var key = lines.length; key--;)
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

		var chis = new Decimal(new Decimal(c_s1.y).minus(c_s0.y)),
		znam = new Decimal(new Decimal(c_s1.x).minus(c_s0.x));
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

	    add_text_v_or_h(a1);
	    add_text_v_or_h(b1);
	    add_text_v_or_h(b2);
	    add_text_diag(index);

	    moveVertexName(b1, b2, obshaya_point(b1, b2));

    	moveVertexName(a1, b1, obshaya_point(a1, b1));

		c.data.fixed = true;
	}
	else if (c.data.d_c_w === 2 && (hitResults0.length === 3 || hitResults1.length === 3))
	{
		line_arr = line_arr_gen(c);

		for (var i = line_arr.length; i--;)
		{
			for (var j = line_arr.length; j--;)
			{
				if (line_arr[i] !== line_arr[j] && obshaya_point(line_arr[i], line_arr[j]) !== null
					&& line_arr[i].strokeWidth === 3 && line_arr[j].strokeWidth === 3)
				{
					if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), c_s0))
					{
						pd0 = c_s0.clone();
						pd1 = c_s1.clone();
					}
					else if (point_ravny(obshaya_point(line_arr[i], line_arr[j]), c_s1))
					{
						pd0 = c_s1.clone();
						pd1 = c_s0.clone();
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

		var chis = new Decimal(new Decimal(c_s1.y).minus(c_s0.y)),
		znam = new Decimal(new Decimal(c_s1.x).minus(c_s0.x));
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
	   		if (!a1.data.fixed)
	   		{
	   			var napr, dop_length;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		if (!a2.data.fixed)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	    	add_text_v_or_h(lines_sort[key]);
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
	else if (c.data.d_c_w === 2)
	{
		line_arr = line_arr_gen(c);

		g_points = getPathsPointsBySort(lines_sort);
		points_m = findMinAndMaxCordinate_g();
		var min_point = new Point(points_m.minX, points_m.minY);
		var max_point = new Point(points_m.maxX, points_m.maxY);
		var center_line = Path.Line(min_point, max_point);
		var center_point = center_line.position.clone();
		center_line.remove();

		pd0 = c_s0.clone();
		pd1 = c_s1.clone();

		var rast0 = Math.sqrt(Math.pow(pd0.x - center_point.x, 2) + Math.pow(pd0.y - center_point.y, 2));
		var rast1 = Math.sqrt(Math.pow(pd1.x - center_point.x, 2) + Math.pow(pd1.y - center_point.y, 2));
		if (rast0 < rast1)
		{
			pd0 = c_s1.clone();
			pd1 = c_s0.clone();
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


		var chis = new Decimal(new Decimal(c_s1.y).minus(c_s0.y)),
		znam = new Decimal(new Decimal(c_s1.x).minus(c_s0.x));
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
	   		if (!a1.data.fixed && a1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		else if (!b1.data.fixed && b1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		if (!a2.data.fixed && a2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		else if (!b2.data.fixed && b2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	    	add_text_v_or_h(lines_sort[key]);
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
	else if (c.data.d_c_w === 1)
	{
		line_arr = line_arr_gen(c);

		for (var i = line_arr.length; i--;)
		{
			if (line_arr[i].strokeWidth === 3)
			{
				if (point_ravny(obshaya_point(line_arr[i], c), c_s0))
				{
					pd0 = c_s0.clone();
					pd1 = c_s1.clone();
				}
				else if (point_ravny(obshaya_point(line_arr[i], c), c_s1))
				{
					pd0 = c_s1.clone();
					pd1 = c_s0.clone();
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


		var chis = new Decimal(new Decimal(c_s1.y).minus(c_s0.y)),
		znam = new Decimal(new Decimal(c_s1.x).minus(c_s0.x));
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
	   		if (!a1.data.fixed && a1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		else if (!b1.data.fixed && b1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		if (!a2.data.fixed && a2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		else if (!b2.data.fixed && b2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	    	add_text_v_or_h(lines_sort[key]);
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
	else if (c.data.d_c_w === 0)
	{
		line_arr = line_arr_gen(c);

		g_points = getPathsPointsBySort(lines_sort);
		points_m = findMinAndMaxCordinate_g();
		var min_point = new Point(points_m.minX, points_m.minY);
		var max_point = new Point(points_m.maxX, points_m.maxY);
		var center_line = Path.Line(min_point, max_point);
		var center_point = center_line.position.clone();
		center_line.remove();

		pd0 = c_s0.clone();
		pd1 = c_s1.clone();

		var rast0 = Math.sqrt(Math.pow(pd0.x - center_point.x, 2) + Math.pow(pd0.y - center_point.y, 2));
		var rast1 = Math.sqrt(Math.pow(pd1.x - center_point.x, 2) + Math.pow(pd1.y - center_point.y, 2));
		if (rast0 < rast1)
		{
			pd0 = c_s1.clone();
			pd1 = c_s0.clone();
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


		var chis = new Decimal(new Decimal(c_s1.y).minus(c_s0.y)),
		znam = new Decimal(new Decimal(c_s1.x).minus(c_s0.x));
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
	   		if (!a1.data.fixed && a1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		else if (!b1.data.fixed && b1.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint1);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		if (!a2.data.fixed && a2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd1, pd0);
	   			var angle_nefixed_diag = get_angle(pd1, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	   		else if (!b2.data.fixed && b2.strokeWidth === 1)
	   		{
	   			var dop_length, napr;
	   			var angle_ch_diag = get_angle(pd0, pd1);
	   			var angle_nefixed_diag = get_angle(pd0, oldObshayaPoint2);
	   			var angle_dd = Decimal.abs(new Decimal(angle_ch_diag).minus(angle_nefixed_diag)).toNumber();
	   			if (angle_dd > 180)
	   			{
	   				angle_dd = new Decimal(360).minus(angle_dd).toNumber();
	   			}

	   			if (+length < c.length && angle_dd < 90 || +length > c.length && angle_dd >= 90)
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
	    	add_text_v_or_h(lines_sort[key]);
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
	    c.data.fixed = true;
	}

	for (var i = lines_sort.length, l_s0, l_s1; i--;)
	{
		l_s0 = lines_sort[i].segments[0].point;
		l_s1 = lines_sort[i].segments[1].point;
		l_s0.x = l_s0.x.toFixed(2)-0;
		l_s0.y = l_s0.y.toFixed(2)-0;
		l_s1.x = l_s1.x.toFixed(2)-0;
		l_s1.y = l_s1.y.toFixed(2)-0;
	}
	for (var i = diag_sort.length, d_s0, d_s1; i--;)
	{
		d_s0 = diag_sort[i].segments[0].point;
		d_s1 = diag_sort[i].segments[1].point;
		d_s0.x = d_s0.x.toFixed(2)-0;
		d_s0.y = d_s0.y.toFixed(2)-0;
		d_s1.x = d_s1.x.toFixed(2)-0;
		d_s1.y = d_s1.y.toFixed(2)-0;
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
		cancel_last_action();
	}
	else
	{
		change_length_diag(main_diag_index, newLength, true);
		if (rek === false)
		{
			diag_sort[main_diag_index].data.fixed = true;
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
	x1 = (new Decimal(x1).plus(xp1_o).toNumber()).toFixed(2)-0;
	x2 = (new Decimal(x2).plus(xp1_o).toNumber()).toFixed(2)-0;
	y1 = (new Decimal(y1).plus(yp1_o).toNumber()).toFixed(2)-0;
	y2 = (new Decimal(y2).plus(yp1_o).toNumber()).toFixed(2)-0;

	return [new Point(x1, y1), new Point(x2, y2)];
}

function line_arr_gen(diag_i)
{
	var line_arr = [];
	var hitResults0 = project.hitTestAll(diag_i.segments[0].point, {class: Path, segments: true, tolerance: 2});
	var hitResults1 = project.hitTestAll(diag_i.segments[1].point, {class: Path, segments: true, tolerance: 2});
	var op;
	for (var key0 = hitResults0.length; key0--;)
	{
		if (lines_ravny(hitResults0[key0].item, diag_i) || hitResults0[key0].item.segments.length !== 2)
		{
			continue;
		}
		for (var key1 = hitResults1.length; key1--;)
		{
			if (!lines_ravny(hitResults1[key1].item, diag_i) && hitResults1[key1].item.segments.length === 2)
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
	var j = 0, p1, p2, rast;
	for (var i = lines_sort.length, l_i, l_j, min_rast; i--;)
	{
		l_i = lines_sort[i];
		if (i === 0)
		{
			j = lines_sort.length - 1;
		}
		else
		{
			j = i - 1;
		}
		l_j = lines_sort[j];
		min_rast = 40000;
		for (var ii = l_i.segments.length, ls_ii; ii--;)
		{
			ls_ii = l_i.segments[ii].point;
			for (var jj = l_j.segments.length, ls_jj; jj--;)
			{
				ls_jj = l_j.segments[jj].point;
				rast = Math.sqrt(Math.pow(ls_ii.x - ls_jj.x, 2) + Math.pow(ls_ii.y - ls_jj.y, 2));
				if (min_rast > rast)
				{
					min_rast = rast;
					p1 = ii;
					p2 = jj;
				}
			}
		}
		l_i.segments[p1].remove();
		l_i.add(l_j.segments[p2].point);
	}

	g_points = getPathsPointsBySort(lines_sort);
	//g_points = changePointsOrderForNaming(g_points, 2, lines_sort);

	for (var i = diag_sort.length, d_i, min_rast_s0, min_rast_s1; i--;)
	{
		d_i = diag_sort[i];
		min_rast_s0 = 40000;
		min_rast_s1 = 40000;
		for (var j = g_points.length, gp_j, ds; j--;)
		{
			gp_j = g_points[j];
			ds = d_i.segments[0].point;
			rast = Math.sqrt(Math.pow(ds.x - gp_j.x, 2) + Math.pow(ds.y - gp_j.y, 2));
			if (min_rast_s0 > rast)
			{
				min_rast_s0 = rast;
				p1 = gp_j;
			}
			ds = d_i.segments[1].point;
			rast = Math.sqrt(Math.pow(ds.x - gp_j.x, 2) + Math.pow(ds.y - gp_j.y, 2));
			if (min_rast_s1 > rast)
			{
				min_rast_s1 = rast;
				p2 = gp_j;
			}
		}
		d_i.removeSegments();
		d_i.addSegments([p1, p2]);
	}
}

function find_part_chert_on_diag(diag_f, index)
{
	var point_n, point_k;
    var obrs = false;
    var obmen;
    var mass_w = [];
    var mass_d = [];
	g_points = getPathsPointsBySort(lines_sort);

	for (var key = g_points.length; key--;)
	{
		if (point_ravny(diag_f.segments[0].point, g_points[key]))
		{
			point_n = key-0;
		}
		if (point_ravny(diag_f.segments[1].point, g_points[key]))
		{
			point_k = key-0;
		}
	}
	if (point_n > point_k)
	{
		obmen = point_n;
		point_n = point_k-0;
		point_k = obmen-0;
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
	}
	else if (e.wheelDelta > 0 && view.zoom < 2.2)
	{
		view.zoom = new Decimal(view.zoom).plus(0.05).toNumber();
	}

	var z = (14 / view.zoom).toFixed(2)-0;
	for (var key = text_points.length; key--;)
	{
		text_points[key].fontSize = z;
	}
	for (var key = text_lines.length; key--;)
	{
		text_lines[key].fontSize = z;
	}
	for (var key = text_diag.length; key--;)
	{
		text_diag[key].fontSize = z;
	}
	for (var key = text_manual_diags.length; key--;)
	{
		text_manual_diags[key].fontSize = z;
	}

	for (var key = lines.length, l_s0, l_s1, l_k; key--;)
	{
		l_k = lines[key];
		l_s0 = l_k.segments[0].point;
		l_s1 = l_k.segments[1].point;
		l_s0.x = l_s0.x.toFixed(2)-0;
		l_s1.x = l_s1.x.toFixed(2)-0;
		l_s0.y = l_s0.y.toFixed(2)-0;
		l_s1.y = l_s1.y.toFixed(2)-0;
		if (text_lines[l_k.data.id] !== undefined)
		{
			text_lines[l_k.data.id].position = l_k.position;
		}
	}

	if (start_point !== undefined && end_point !== undefined)
	{
		start_point.x = start_point.x.toFixed(2)-0;
		start_point.y = start_point.y.toFixed(2)-0;
		end_point.x = end_point.x.toFixed(2)-0;
		end_point.y = end_point.y.toFixed(2)-0;
	}

	for (var key = diag_sort.length; key--;)
	{
		if (text_diag[key] !== undefined)
		{
			text_diag[key].position = diag_sort[key].position;
		}
	}
	resize_canvas();
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
				view.zoom = new Decimal(view.zoom).minus(0.025).toNumber();
			}
			else if (rast2 > rast && view.zoom < 2.2)
			{
				view.zoom = new Decimal(view.zoom).plus(0.025).toNumber();
			}

			var z = (14 / view.zoom).toFixed(2)-0;
			for (var key = text_points.length; key--;)
			{
				text_points[key].fontSize = z;
			}
			for (var key = text_lines.length; key--;)
			{
				text_lines[key].fontSize = z;
			}
			for (var key = text_diag.length; key--;)
			{
				text_diag[key].fontSize = z;
			}
			for (var key = text_manual_diags.length; key--;)
			{
				text_manual_diags[key].fontSize = z;
			}

			rast = Math.sqrt(Math.pow(event.touches[1].pageX - event.touches[0].pageX, 2) + Math.pow(event.touches[1].pageY - event.touches[0].pageY, 2));
		}

		for (var key = lines.length, l_s0, l_s1, l_k; key--;)
		{
			l_k = lines[key];
			l_s0 = l_k.segments[0].point;
			l_s1 = l_k.segments[1].point;
			l_s0.x = l_s0.x.toFixed(2)-0;
			l_s1.x = l_s1.x.toFixed(2)-0;
			l_s0.y = l_s0.y.toFixed(2)-0;
			l_s1.y = l_s1.y.toFixed(2)-0;
			if (text_lines[l_k.data.id] !== undefined)
			{
				text_lines[l_k.data.id].position = l_k.position;
			}
		}

		if (start_point !== undefined && end_point !== undefined)
		{
			start_point.x = start_point.x.toFixed(2)-0;
			start_point.y = start_point.y.toFixed(2)-0;
			end_point.x = end_point.x.toFixed(2)-0;
			end_point.y = end_point.y.toFixed(2)-0;
		}

		for (var key = diag_sort.length; key--;)
		{
			if (text_diag[key] !== undefined)
			{
				text_diag[key].position = diag_sort[key].position;
			}
		}
	}
	resize_canvas();
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

	document.getElementById('cancelLastAction').onclick = cancel_last_action;
	document.getElementById('cancelLastAction2').onclick = cancel_last_action;
	document.getElementById('reset').onclick = clear_elem;
	document.getElementById('reset2').onclick = clear_elem;
	document.getElementById('back').onclick = go_back;
	document.getElementById('back2').onclick = go_back;
	document.getElementById('open_popup').onclick = open_popup;
	document.getElementById('open_popup2').onclick = open_popup;
	document.getElementById('ok').onclick = elem_button_ok_onclick;
	document.getElementById('ok2').onclick = elem_button_ok_onclick;

	if (window.innerWidth > window.innerHeight) // для мониторов
	{
		elem_newLength = document.getElementById('newLength2');
		elem_useGrid = document.getElementById('useGrid2');
	}
	else //для мобильных
	{
		elem_newLength = document.getElementById('newLength');
		elem_useGrid = document.getElementById('useGrid');
	}

	function elem_button_ok_onclick()
	{
		ok_enter_all();
	}

	document.onwheel = wheel_zoom;
	document.getElementById('myCanvas').addEventListener('touchmove', touch_zoom, false);
	document.getElementById('close_sketch').onclick = close_sketch_click_all;
	elem_jform_n4 = document.getElementById('jform_n4');
	elem_jform_n5 = document.getElementById('jform_n5');
	elem_jform_n9 = document.getElementById('jform_n9');
	elem_window = document.getElementById('window');
	elem_preloader = document.getElementById('preloader');

	document.getElementById('num0').onclick = elem_num_onclick;
	document.getElementById('num02').onclick = elem_num_onclick;
	document.getElementById('num1').onclick = elem_num_onclick;
	document.getElementById('num12').onclick = elem_num_onclick;
	document.getElementById('num2').onclick = elem_num_onclick;
	document.getElementById('num22').onclick = elem_num_onclick;
	document.getElementById('num3').onclick = elem_num_onclick;
	document.getElementById('num32').onclick = elem_num_onclick;
	document.getElementById('num4').onclick = elem_num_onclick;
	document.getElementById('num42').onclick = elem_num_onclick;
	document.getElementById('num5').onclick = elem_num_onclick;
	document.getElementById('num52').onclick = elem_num_onclick;
	document.getElementById('num6').onclick = elem_num_onclick;
	document.getElementById('num62').onclick = elem_num_onclick;
	document.getElementById('num7').onclick = elem_num_onclick;
	document.getElementById('num72').onclick = elem_num_onclick;
	document.getElementById('num8').onclick = elem_num_onclick;
	document.getElementById('num82').onclick = elem_num_onclick;
	document.getElementById('num9').onclick = elem_num_onclick;
	document.getElementById('num92').onclick = elem_num_onclick;

	function elem_num_onclick()
	{
		if (!first_click)
		{
			elem_newLength.value = "";
			first_click = true;
		}
		elem_newLength.value += this.innerHTML;
	};

	document.getElementById('numback').onclick = elem_numback_onclick;
	document.getElementById('numback2').onclick = elem_numback_onclick;

	function elem_numback_onclick()
	{
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
	document.getElementById('myCanvas').oncontextmenu = function()
	{
		if (elem_useGrid.checked)
		{
			elem_useGrid.checked = false;
		}
		return false;
	};
	document.getElementById('triangulate_auto').onclick = function()
	{
		triangulate_rezhim = 1;
		document.getElementById('popup2').style.display = 'none';
		elem_window.style.display = 'block';
		resize_canvas();
		ok_enter_all();
	};
	document.getElementById('triangulate_manual').onclick = function()
	{
		triangulate_rezhim = 2;
		document.getElementById('popup2').style.display = 'none';
		elem_window.style.display = 'none';
		resize_canvas();

		var cir, op;
		for (var i = text_points.length; i--;)
		{
			op = obshaya_point(lines[text_points[i].data.id_line1], lines[text_points[i].data.id_line2]);
			cir = new Path.Circle(op, 5);
			cir.fillColor = 'blue';
			text_points[i].data.circle = cir;
		}

		clicks_pt();
		save_cancel();
	};
}

function sort_sten()
{
	lines_sort = [];
	g_points = getPathsPoints(lines);
	g_points = changePointsOrderForNaming(g_points, 2, lines);
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
		for (var key = lines.length; key--;)
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
                    fontFamily: 'lucida console',
                    fontWeight: 'bold',
                    fontSize: (14 / view.zoom).toFixed(2)-0
                });
            textPoints.push(pt);
            for (var key = lines.length; key--;)
            {
            	for (var j in lines[key].segments)
            	{
            		if (point_ravny(namedPoints[i].point, lines[key].segments[j].point))
            		{
            			id1 = key;
            		}
            	}
            }
            for (var key = lines.length; key--;)
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
        	pt.data.id_line1 = +id1;
        	pt.data.id_line2 = +id2;
        }
    }
    return textPoints;
}

function createVertexNames()
{
    var namedPoints = [];
    var allpoints = changePointsOrderForNaming(getPathsPoints(lines), 1, lines);
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

function findWallsByPoint(points, point, flag, lines)
{
	var temp_point = [];
	var j = 0, min_y, rez_point;
	if (flag === 1)
	{
		for(var j in lines)
		{
			if (point_ravny(lines[j].getFirstSegment().point, point) || point_ravny(lines[j].getLastSegment().point, point))
			{
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
		for (var key = temp_point.length; key--;)
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

function changePointsOrderForNaming(points, flag, lines)
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
                point = findWallsByPoint(result, result[result.length - 1], 1, lines);
                points.splice(points.indexOf(point),1);
                result.push(point);
            }
            else
            {
                point = findWallsByPoint(result, result[result.length - 1], 2, lines);
                points.splice(points.indexOf(point),1);
                result.push(point);
            }

        }
        return result;
    }
}

function getPathsPointsBySort(lines)
{
	var allpoints = [];
	var bj;
	for (var j = 0; j < lines.length; j++)
	{
		if (j === 0)
		{
			bj = lines.length - 1;
		}
		else
		{
			bj = j - 1;
		}
		allpoints.push(obshaya_point(lines[j], lines[bj]));
	}
	return allpoints;
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

var square_tkan, lines_tkan = [];
function tkan()
{
	var polygonVertices = [], polygons, paddingPolygon, x1, y1, x2, y2, intersections;
	g_points = getPathsPointsBySort(lines_sort);
	for(var i = g_points.length; i--;)
	{
		polygonVertices.push({x: g_points[i].x, y: g_points[i].y});
	}
	polygons = init_pol(polygonVertices);

	paddingPolygon = polygons.marginPolygon;

	var chertezh = new Path();
	for(var i = paddingPolygon.edges.length; i--;)
	{
		x1 = paddingPolygon.edges[i].vertex1.x;
		y1 = paddingPolygon.edges[i].vertex1.y;
		x2 = paddingPolygon.edges[i].vertex2.x;
		y2 = paddingPolygon.edges[i].vertex2.y;
		lines_tkan.push(Path.Line(new Point(x1, y1), new Point(x2, y2)));
		//lines_tkan[lines_tkan.length - 1].strokeColor = 'red';
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
		lines_tkan[lines_tkan.length - 1].data.id = lines_tkan.length - 1;
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
            fontFamily: 'lucida console',
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
        			id1 = lines_tkan[i].data.id;
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
        			id2 = lines_tkan[i].data.id;
        		}
        	}
        }
        pt.data.id_line1 = +id1;
        pt.data.id_line2 = +id2;
		c_i++;
	}

	var angle_final, j_f, sq_min = 100222, sq_polo, sq_obr;
	/*for (var i = lines_tkan.length; i--;)
	{
		lines_tkan[i].rotate(-1, view.center);
	}*/
	for (var angle_rotate = 0; angle_rotate < 360; angle_rotate++)
	{
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
			sq_obr = sq_obr.toFixed(2)-0;

			if (sq_obr < sq_min)
			{
				sq_min = sq_obr;
				angle_final = angle_rotate;
				j_f = j;
				//console.log(sq_obr, angle_final);
			}
		}
		for (var i = lines_tkan.length; i--;)
		{
			lines_tkan[i].rotate(1, view.center);
		}
	}
	/*for (var i = lines_tkan.length; i--;)
	{
		lines_tkan[i].rotate(180, view.center);
	}*/
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
	square_obrezkov = sq_obr.toFixed(2)-0;
	//console.log('sq_obr', square_obrezkov);

	width_final = width_polotna[j_f].width;
	sq_polotna = sq_polo;
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
		kx = kx.toFixed(1)-0;
		ky = ky.toFixed(1)-0;
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
			tc.fontSize = (14 / view.zoom).toFixed(2)-0;
			tc.content = Math.round(arr[i].length);
			var angle = (Math.atan((arr[i].segments[1].point.y-arr[i].segments[0].point.y)/(arr[i].segments[1].point.x
				-arr[i].segments[0].point.x))*180)/Math.PI;
		    tc.rotate(angle);
		    tc.position = arr[i].position;
		    tc.bringToFront();
		    text_contur.push(tc);
		}
	}
}

function moveVertexNameContur(line1, line2, newPoint)
{
	var pt_id;
	for (var key in text_points)
	{
		if (text_points[key].data.id_line1 === line1.data.id && text_points[key].data.id_line2 === line2.data.id
			|| text_points[key].data.id_line2 === line1.data.id && text_points[key].data.id_line1 === line2.data.id)
		{
			text_points[key].point = new Point(newPoint.x, newPoint.y);
			break;
		}
	}
}

function cuts_gen()
{
	cuts_json = [];
	for (var i = polotna.length; i--;)
	{
		for (var j = polotna[i].cuts.children.length; j--;)
		{
			if (polotna[i].cuts.children[j].area > 10000)
			{
				cuts_json.push(polotna[i].cuts.children[j].exportJSON({asString: false}));
			}
		}
	}
	cuts_json = JSON.stringify(cuts_json);
}

save_cancel();

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
		lines.push(obj);
		obj.data.id = lines.length - 1;
	}
	for (var i = diags_points_rec.length; i--;)
	{
		obj = new Path.Line(new Point(diags_points_rec[i].s0_x, diags_points_rec[i].s0_y),
			new Point(diags_points_rec[i].s1_x, diags_points_rec[i].s1_y));
		obj.strokeColor = 'green';
		obj.strokeWidth = 1;
		diag_sort.push(obj);
		diag.push(obj);
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
                fontFamily: 'lucida console',
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
						id1 = lines_sort[j].data.id;
						id2 = lines_sort[k].data.id;
						break;
					}
				}
			}
		}
		obj.data.id_line1 = +id1;
		obj.data.id_line2 = +id2;
	}
	code = code_char;
	alfavit = alfavit_num;
	zerkalo(1);
	close_sketch_click_all();
}
//build_chert();
});