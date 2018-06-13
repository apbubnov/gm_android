jQuery(document).ready(function()
{
var reg_url = /gm-vrn/;
if (!reg_url.test(location.href))
{
	window.history.back();
}
var lines = [];
var diags = [];
var text_points = [];
var text_lines = [];
var down_g, right_g, up_g, left_g, granica;
var g_points = [];
var angle_rotate = 0, p_usadki = 0.92;
var elem_ugol, elem_usadka, elem_width;
var width_poloten = [
					{width: 500},
					{width: 450},
					{width: 400},
					{width: 360},
					{width: 320},
					{width: 270},
					{width: 240},
					{width: 220},
					{width: 200},
					{width: 150},
					{width: 140}
					];
var polotna = [], points_poloten = [], koordinats_poloten = [], begin_point_raskroy_y, count_dop_pt = 0, start_code, start_alfavit, count_polos = 1;
var cuts = [], sq_polotna, chertezh_area_usadka, sq_obrezkov, cuts_json, timer_dvig, timer_rotate, text_diag = [], seam_lines = [];
var touch1, touch2;

paper.install(window);
paper.setup("myCanvas");
var tool = new Tool();
resize_canvas();
jQuery(window).resize(resize_canvas);
project.activeLayer.applyMatrix = false;
jQuery('#myCanvas').css('resize', 'both');
begin();

function begin()
{

	var polotna = fun_canv.getGreeting();

    js_polotna = JSON.parse(polotna);
    width_polotna = js_polotna;

	width_poloten = width_polotna;

	var code = fun_canv.get_code();
	var alfavit = fun_canv.get_alfavit();



	start_code = code;
	start_alfavit = alfavit;

	var obj, pt_name;
	for (var i = walls_points.length; i--;)
	{
		obj = new Path.Line(new Point(walls_points[i].s0_x, walls_points[i].s0_y), 
			new Point(walls_points[i].s1_x, walls_points[i].s1_y));
		obj.strokeColor = 'green';
		obj.strokeWidth = 3;
		lines.push(obj);
		obj.data.id = lines.length - 1;
	}
	for (var i = diags_points.length; i--;)
	{
		obj = new Path.Line(new Point(diags_points[i].s0_x, diags_points[i].s0_y), 
			new Point(diags_points[i].s1_x, diags_points[i].s1_y));
		obj.strokeColor = 'green';
		obj.strokeWidth = 1;
		diags.push(obj);
	}
	var code_char = 64, alfavit_num = 0, id1, id2, op;
	for (var i = 0; i < pt_points.length; i++)
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
                point: new Point(pt_points[i].x, pt_points[i].y),
                content: pt_name,
                fillColor: 'blue',
                justification: 'center',
                fontFamily: 'lucida console',
                fontWeight: 'bold',
                fontSize : (14 / view.zoom).toFixed(2)-0
            });
		text_points.push(obj);

		op = new Point(pt_points[i].x + 10, pt_points[i].y + 5);
		for (var j = lines.length; j--;)
		{
			for (var k = lines.length; k--;)
			{
				if (obshaya_point(lines[j], lines[k]) !== null)
				{
					if (point_ravny(obshaya_point(lines[j], lines[k]), op))
					{
						obj.data.id_line1 = j;
        				obj.data.id_line2 = k;
						break;
					}
				}
			}
		}
	}

	sdvig();
	zoom(1);

	var chertezh = new CompoundPath();

	chertezh.addChildren(lines);
	chertezh.addChildren(diags);
	chertezh.position = view.center;
	project.activeLayer.addChildren(lines);
	project.activeLayer.addChildren(diags);
	chertezh.remove();

	for (var i = lines.length; i--;)
	{
		lines[i].segments[0].point.x = +lines[i].segments[0].point.x.toFixed(2);
		lines[i].segments[0].point.y = +lines[i].segments[0].point.y.toFixed(2);
		lines[i].segments[1].point.x = +lines[i].segments[1].point.x.toFixed(2);
		lines[i].segments[1].point.y = +lines[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diags.length; i--;)
	{
		diags[i].segments[0].point.x = +diags[i].segments[0].point.x.toFixed(2);
		diags[i].segments[0].point.y = +diags[i].segments[0].point.y.toFixed(2);
		diags[i].segments[1].point.x = +diags[i].segments[1].point.x.toFixed(2);
		diags[i].segments[1].point.y = +diags[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();

	var j = 0;
    for (var i = 0; i < lines.length; i++)
    {
    	if (i === 0)
    	{
    		j = lines.length - 1;
    	}
    	else
    	{
    		j = i - 1;
    	}
    	if (lines[i] !== lines[j] && obshaya_point(lines[i], lines[j]) !== null)
    	{
    		moveVertexName(lines[i], lines[j], obshaya_point(lines[i], lines[j]));
    	}
    }

    elem_width = document.getElementById('width_polotna');
    var option;
    for (var i = width_poloten.length; i--;)
    {
		option = document.createElement('option');
		option.value = i;
		option.text = width_poloten[i].width;
		elem_width.add(option);
    }

    elem_ugol = document.getElementById('ugol');
    jQuery('#ugol').bind('keyup mouseup touchend', rotate_chert_click);

    elem_usadka = document.getElementById('usadka');
    jQuery('#usadka').bind('keyup mouseup touchend', change_usadka);
    
    elem_width.onchange = function()
    {
    	g_points = getPathsPointsBySort(lines);
		var points_m = findMinAndMaxCordinate_g();
		begin_point_raskroy_y = points_m.maxY;

		count_polos = Math.ceil((points_m.maxY - points_m.minY) / (width_poloten[elem_width.value].width / p_usadki));
		add_polotno();
	};

    jQuery('#up').bind('mousedown touchstart', function()
    {
    	clearInterval(timer_dvig);
    	timer_dvig = setInterval(up_click, 10);
    });
    jQuery('#down').bind('mousedown touchstart', function()
    {
    	clearInterval(timer_dvig);
    	timer_dvig = setInterval(down_click, 10);
    });
    jQuery('#up').bind('mouseup touchend', function()
    {
    	clearInterval(timer_dvig);
    });
    jQuery('#down').bind('mouseup touchend', function()
    {
    	clearInterval(timer_dvig);
    });

    jQuery('#plus').bind('mousedown touchstart', function()
    {
    	clearInterval(timer_rotate);
    	timer_rotate = setInterval(
    		function()
    		{
		    	elem_ugol.value++;
		    	rotate_chert_click();
    		}, 10);
    });
   	jQuery('#minus').bind('mousedown touchstart', function()
    {
    	clearInterval(timer_rotate);
    	timer_rotate = setInterval(
    		function()
    		{
		    	elem_ugol.value--;
		    	rotate_chert_click();
    		}, 10);
    });
    jQuery('#plus').bind('mouseup touchend', function()
    {
    	clearInterval(timer_rotate);
    });
    jQuery('#minus').bind('mouseup touchend', function()
    {
    	clearInterval(timer_rotate);
    });

    g_points = getPathsPointsBySort(lines);
	var points_m = findMinAndMaxCordinate_g();
	begin_point_raskroy_y = points_m.maxY;

    count_polos = 0;
    add_polotno();
    add_text_lines();

    document.onwheel = wheel_zoom;
	document.getElementById('myCanvas').addEventListener('touchmove', touch_zoom, false);
	document.getElementById('open_popup').onclick = send_and_save;
}

function resize_canvas()
{
	//jQuery("#myCanvas").css("width", document.body.clientWidth - 20);
	//jQuery("#myCanvas").css("height", document.body.clientHeight - 120);
	view.viewSize = new Size(parseFloat(jQuery("#myCanvas").css("width")), parseFloat(jQuery("#myCanvas").css("height")));
	view.center = new Point(Math.round(view.viewSize.width / 2), Math.round(view.viewSize.height / 2));

	var chertezh = new CompoundPath();

	chertezh.addChildren(project.activeLayer.children);
	project.activeLayer.removeChildren();
	
	chertezh.position=view.center;

	project.activeLayer.addChildren(chertezh.children);
	chertezh.remove();

	zoom(1);

	for (var i = lines.length; i--;)
	{
		lines[i].segments[0].point.x = lines[i].segments[0].point.x.toFixed(2)-0;
		lines[i].segments[0].point.y = lines[i].segments[0].point.y.toFixed(2)-0;
		lines[i].segments[1].point.x = lines[i].segments[1].point.x.toFixed(2)-0;
		lines[i].segments[1].point.y = lines[i].segments[1].point.y.toFixed(2)-0;
	}
	for (var i = diags.length; i--;)
	{
		diags[i].segments[0].point.x = diags[i].segments[0].point.x.toFixed(2)-0;
		diags[i].segments[0].point.y = diags[i].segments[0].point.y.toFixed(2)-0;
		diags[i].segments[1].point.x = diags[i].segments[1].point.x.toFixed(2)-0;
		diags[i].segments[1].point.y = diags[i].segments[1].point.y.toFixed(2)-0;
	}
	okruglenie_all_segments();

	var j = 0;
    for (var i = 0; i < lines.length; i++)
    {
    	if (i === 0)
    	{
    		j = lines.length - 1;
    	}
    	else
    	{
    		j = i - 1;
    	}
    	if (lines[i] !== lines[j] && obshaya_point(lines[i], lines[j]) !== null)
    	{
    		moveVertexName(lines[i], lines[j], obshaya_point(lines[i], lines[j]));
    	}
    }
}

document.getElementById('back').onclick = function()
{
	document.getElementById('preloader').style.display = 'block';
	if (page === "guild")
	{
		document.getElementById('form_redactor').submit();
	}
	else
	{
    	location.href = url;
    }
};

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

function zoom(flag1)
{
	var z;
	if (lines.length === 0)
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
	g_points = getPathsPointsBySort(lines);
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
					for (var key in text_lines)
					{
						text_lines[key].fontSize = z;
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
				for (var key in text_lines)
				{
					text_lines[key].fontSize = z;
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

function findMinAndMaxCordinate_g()
{
    if(g_points.length > 0){
        var minX = g_points[0].x;
        var maxX = g_points[0].x;
        var minY = g_points[0].y;
        var maxY = g_points[0].y;
        for(var i = 1; i < g_points.length; i++)
        {
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

function sdvig()
{
	if (lines.length === 0)
	{
		return;
	}

	var sx, sy, points_m;
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
	g_points = getPathsPointsBySort(lines);
	
	points_m = findMinAndMaxCordinate_g();

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
	for (var key = lines.length, l_s0, l_s1; key--;)
	{
		l_s0 = lines[key].segments[0].point;
		l_s1 = lines[key].segments[1].point;
		l_s0.x = l_s0.x.toFixed(2)-0;
		l_s1.x = l_s1.x.toFixed(2)-0;
		l_s0.y = l_s0.y.toFixed(2)-0;
		l_s1.y = l_s1.y.toFixed(2)-0;
	}
	g_points = getPathsPointsBySort(lines);
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

function okruglenie_all_segments()
{
	var j = 0, p1, p2, rast;
	for (var i = lines.length, l_i, l_j, min_rast; i--;)
	{
		l_i = lines[i];
		if (i === 0)
		{
			j = lines.length - 1;
		}
		else
		{
			j = i - 1;
		}
		l_j = lines[j];
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
	
	g_points = getPathsPointsBySort(lines);

	for (var i = diags.length, d_i, min_rast_s0, min_rast_s1; i--;)
	{
		d_i = diags[i];
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

function rotate_chert_click()
{
	var regexp = /^[\-\d]+$/;
	var chertezh;

	if (regexp.test(elem_ugol.value))
	{
		if (elem_ugol.value < 0)
		{

			elem_ugol.value = +elem_ugol.value + 360;
		}
		else if (elem_ugol.value > 359)
		{
			elem_ugol.value = +elem_ugol.value - 360;
		}
		angle_rotate = elem_ugol.value - angle_rotate;

		for (var i = lines.length; i--;)
		{
			lines[i].rotate(angle_rotate, view.center);

			//lines[i].segments[0].point.x = +lines[i].segments[0].point.x.toFixed(2);
			//lines[i].segments[0].point.y = +lines[i].segments[0].point.y.toFixed(2);
			//lines[i].segments[1].point.x = +lines[i].segments[1].point.x.toFixed(2);
			//lines[i].segments[1].point.y = +lines[i].segments[1].point.y.toFixed(2);
		}
		for (var i = diags.length; i--;)
		{
			diags[i].rotate(angle_rotate, view.center);

			//diags[i].segments[0].point.x = +diags[i].segments[0].point.x.toFixed(2);
			//diags[i].segments[0].point.y = +diags[i].segments[0].point.y.toFixed(2);
			//diags[i].segments[1].point.x = +diags[i].segments[1].point.x.toFixed(2);
			//diags[i].segments[1].point.y = +diags[i].segments[1].point.y.toFixed(2);
		}
		okruglenie_all_segments();

		angle_rotate = +elem_ugol.value;

		var j = 0;
	    for (var i = 0; i < lines.length; i++)
	    {
	    	if (i === 0)
	    	{
	    		j = lines.length - 1;
	    	}
	    	else
	    	{
	    		j = i - 1;
	    	}
	    	if (lines[i] !== lines[j] && obshaya_point(lines[i], lines[j]) !== null)
	    	{
	    		moveVertexName(lines[i], lines[j], obshaya_point(lines[i], lines[j]));
	    	}
	    }

	    add_text_lines();

		g_points = getPathsPointsBySort(lines);
		var points_m = findMinAndMaxCordinate_g();
		begin_point_raskroy_y = points_m.maxY;
		count_polos = Math.ceil((points_m.maxY - points_m.minY) / polotna[0].polotno.bounds.height);
		add_polotno();
	}
}

var fix_point_dvig;
tool.onMouseDown = function(event)
{
	fix_point_dvig = event.point;
};

tool.onMouseDrag = function(event)
{
	var rast = Math.abs(event.point.y - fix_point_dvig.y);
	g_points = getPathsPointsBySort(lines);
	points_m = findMinAndMaxCordinate_g();
	if (event.point.y < fix_point_dvig.y && points_m.minY < begin_point_raskroy_y - rast - p_width)
	{
		begin_point_raskroy_y -= rast;
	}
	else if (event.point.y > fix_point_dvig.y && points_m.maxY > begin_point_raskroy_y + rast - p_width)
	{
		begin_point_raskroy_y += rast;
	}

	add_polotno();
	fix_point_dvig = event.point;
};

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

function change_usadka()
{
	if (+elem_usadka.value < 1 || +elem_usadka.value > 30)
	{
		//alert('Недопустимая усадка!');
		var x = new Decimal(p_usadki).times(100);
		x = new Decimal(100).minus(x).toNumber();
		//console.log(x);
		//elem_usadka.value = x;
		return;
	}

	p_usadki = new Decimal(100).minus(+elem_usadka.value);
	p_usadki = p_usadki.dividedBy(100).toNumber();
	add_text_lines();
	add_polotno();
}

function add_text_v_or_h(line)
{
	t_l = text_lines[line.data.id];
	if (t_l !== undefined)
	{
		t_l.remove();
	}

	t_l = new PointText();
	t_l.fontFamily = 'arial';
	t_l.fontWeight = 'bold';
	t_l.fontSize = (14 / view.zoom).toFixed(2)-0;
	t_l.content = Math.round(line.length);
	var angle = (Math.atan((line.segments[1].point.y-line.segments[0].point.y)/(line.segments[1].point.x
		-line.segments[0].point.x))*180)/Math.PI;
    t_l.rotate(angle);
    t_l.position = line.position;
    t_l.bringToFront();
}

function add_text_lines()
{
	var len;
	for (var i = lines.length; i--;)
	{
		if (text_lines[i] !== undefined)
		{
			text_lines[i].remove();
		}
		len = new Decimal(lines[i].length).times(p_usadki).toNumber();
		text_lines[i] = new PointText(
			{
                point: new Point(lines[i].position.x, lines[i].position.y),
                content: len.toFixed(1),
                fillColor: 'black',
                justification: 'center',
                fontFamily: 'arial',
                fontWeight: 'bold',
                fontSize : (14 / view.zoom).toFixed(2)-0
            });

		var angle_line = (Math.atan((lines[i].segments[1].point.y-lines[i].segments[0].point.y)/(lines[i].segments[1].point.x-lines[i].segments[0].point.x))*180)/Math.PI;
    	text_lines[i].rotate(angle_line);
    	text_lines[i].position = lines[i].position;
	}
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
	text_diag[index].content = Math.round(diags[index].length);
	var angle = (Math.atan((diags[index].segments[1].point.y-diags[index].segments[0].point.y)/(diags[index].segments[1].point.x
			-diags[index].segments[0].point.x))*180)/Math.PI;
    text_diag[index].rotate(angle);
    text_diag[index].position = new Point(diags[index].position.x, diags[index].position.y);
    text_diag[index].bringToFront();
}

function add_polotno()
{
	//document.getElementById('open_popup').disabled = true;
	remove_pt_intersects();
	code = start_code;
	alfavit = start_alfavit;
	p_width = width_poloten[+elem_width.value].width;

	g_points = getPathsPointsBySort(lines);

	if (begin_point_raskroy_y === undefined)
	{
		var points_m = findMinAndMaxCordinate_g();
		add_rolik(p_width, p_usadki, count_polos, points_m.maxY);
	}
	else
	{
		add_rolik(p_width, p_usadki, count_polos, begin_point_raskroy_y);
	}

}

function add_rolik(p_width, p_usadki, kolvo_poloten, down_y)
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
		if (kolvo_polos > kolvo_poloten && kolvo_poloten !== 0)
		{
			break;
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

	for (var i = polotna.length; i--;)
	{
		for (var j = polotna.length; j--;)
		{
			if (polotna[i] != polotna[j] &&
				polotna[i].polotno.position.y + 1 > polotna[j].polotno.position.y &&
				polotna[i].polotno.position.y - 1 < polotna[j].polotno.position.y &&
				polotna[i].polotno.intersects(polotna[j].polotno))
			{
				obj_parts = polotna[i].parts.unite(polotna[j].parts);
				obj_polotno = polotna[i].polotno.unite(polotna[j].polotno);
				obj_cuts = obj_polotno.exclude(obj_parts);

				polotna[i].parts.remove();
				polotna[i].cuts.remove();
				polotna[i].polotno.remove();

				polotna[j].parts.remove();
				polotna[j].cuts.remove();
				polotna[j].polotno.remove();

				polotna[i].parts = obj_parts;
				polotna[i].cuts = obj_cuts;
				polotna[i].polotno = obj_polotno;
				
				polotna.splice(j, 1);
				if (j < i)
				{
					i--;
				}
			}
		}
	}
	for (var i = polotna.length; i--;)
	{
		draw_rolik(i);
	}
	chertezh.remove();
	return true;
}

function draw_rolik(i)
{
	polotna[i].polotno.strokeColor = 'red';
	polotna[i].polotno.fillColor = null;
	polotna[i].polotno.dashArray = [10, 4];
	polotna[i].polotno.opacity = 1;
	polotna[i].polotno.selected = false;

	polotna[i].parts.strokeColor = null;
	polotna[i].parts.fillColor = 'green';
	polotna[i].parts.dashArray = [];
	polotna[i].parts.opacity = 0.4;
	polotna[i].parts.selected = true;

	polotna[i].cuts.strokeColor = null;
	polotna[i].cuts.fillColor = 'red';
	polotna[i].cuts.dashArray = [];
	polotna[i].cuts.opacity = 0.3;
	polotna[i].cuts.selected = false;
}

function up_click()
{
	g_points = getPathsPointsBySort(lines);
	var points_m = findMinAndMaxCordinate_g();
	if (points_m.minY < begin_point_raskroy_y - p_width)
	{
	    begin_point_raskroy_y -= 0.2;
	    add_polotno();
	}
}

function down_click()
{
	g_points = getPathsPointsBySort(lines);
	var points_m = findMinAndMaxCordinate_g();
	if (points_m.maxY > begin_point_raskroy_y - p_width)
	{
		begin_point_raskroy_y += 0.2;
    	add_polotno();
	}
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
	kx = +kx.toFixed(1);
	ky = +ky.toFixed(1);
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

function svg_gen(flag)
{
	view.zoom = 1;

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
			if (diags[i].length < 40)
			{
				text_diag[i].fontSize -= 1;
				if (diags[i].length < 30)
				{
					text_diag[i].fontSize -= 1;
					if (diags[i].length < 20)
					{
						text_diag[i].fontSize -= 1;
						if (diags[i].length < 10)
						{
							text_diag[i].fontSize -= 1;
						}
					}
				}
			}
			text_diag[i].position = diags[i].position;
			text_diag[i].bringToFront();
		}
		for(var i in lines)
		{
			text_lines[lines[i].data.id].fontSize = 14;
			if (lines[i].length < 40)
			{
				text_lines[lines[i].data.id].fontSize -= 1;
				if (lines[i].length < 30)
				{
					text_lines[lines[i].data.id].fontSize -= 1;
					if (lines[i].length < 20)
					{
						text_lines[lines[i].data.id].fontSize -= 1;
						if (lines[i].length < 10)
						{
							text_lines[lines[i].data.id].fontSize -= 1;
						}
					}
				}
			}
			text_lines[lines[i].data.id].position = lines[i].position;
			text_lines[lines[i].data.id].bringToFront();
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

var calc_img, cut_img, lines_length = [];

function send_and_save()
{
	var sq_polo = 0, sq_obr = 0;
	get_koordinats_poloten(p_usadki);
	//console.log(koordinats_poloten);

	document.getElementById('preloader').style.display = 'block';
	//console.log(clcid);
	//console.log(width_poloten[elem_width.value].width);
	//console.log(p_usadki);
	
	cut_img = svg_gen(2);

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

		sq_polo = new Decimal(Math.abs(polotna[key].polotno.area)).plus(sq_polo).toNumber();
	}

	sq_obr = new Decimal(sq_obr).times(p_usadki).times(p_usadki).toNumber();
	sq_polo = new Decimal(sq_polo).times(p_usadki).times(p_usadki).toNumber();
	sq_obr = new Decimal(sq_obr).dividedBy(10000).toNumber();
	sq_polo = new Decimal(sq_polo).dividedBy(10000).toNumber();

	sq_polotna = sq_polo;
	sq_obrezkov = sq_obr;

	cuts_gen();

    remove_none_shvy();
	rotate_final();
	zerkalo(1);
	remove_pt_intersects();
	
	var l1, l2, lc;
	lines_length = [];
	for (var i = 0; i < lines.length; i++)
	{
		l1 = "";
		l2 = "";
		for (var j = text_points.length; j--;)
		{
			if (point_ravny(new Point(lines[i].segments[0].point.x - 10, lines[i].segments[0].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(lines[i].segments[0].point.x + 10, lines[i].segments[0].point.y - 5), text_points[j].point))
			{
				l1 = text_points[j].content;
			}
			if (point_ravny(new Point(lines[i].segments[1].point.x - 10, lines[i].segments[1].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(lines[i].segments[1].point.x + 10, lines[i].segments[1].point.y - 5), text_points[j].point))
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
		lines_length.push({name: l1 + l2, length: Math.round(lines[i].length)});
	}
	for (var i in diags)
	{
		l1 = "";
		l2 = "";
		for (var j = text_points.length; j--;)
		{
			if (point_ravny(new Point(diags[i].segments[0].point.x - 10, diags[i].segments[0].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(diags[i].segments[0].point.x + 10, diags[i].segments[0].point.y - 5), text_points[j].point))
			{
				l1 = text_points[j].content;
			}
			if (point_ravny(new Point(diags[i].segments[1].point.x - 10, diags[i].segments[1].point.y - 5), text_points[j].point)
				|| point_ravny(new Point(diags[i].segments[1].point.x + 10, diags[i].segments[1].point.y - 5), text_points[j].point))
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
		lines_length.push({name: l1+l2, length: Math.round(diags[i].length)});
	}

	calc_img = svg_gen(1);

	ajax();
}

function ajax()
{
    jQuery.ajax({
        url: "/index.php?option=com_gm_ceiling&task=sketch.save_data_from_sketch",
        async: false,
        data:{
            calc_img: btoa(calc_img),
            cut_img: btoa(cut_img),
            arr_length: lines_length,
            arr_points: koordinats_poloten,
            jform_n4: n4,
            jform_n5: n5,
            jform_n9: n9,
            user_id: uid,
            calc_id: clcid,
            texture: tre,
            color: col,
            manufacturer: man,
            width: width_poloten[elem_width.value].width,
            p_usadki: p_usadki,
            square_obrezkov: sq_obrezkov,
            cuts: cuts_json,
            sq_polo: sq_polotna,
            walls_points: walls_points,
        	diags_points: diags_points,
        	pt_points: pt_points,
        	code: code,
        	alfavit: alfavit
        },
        type:"post",
        success: function(data)
        {
        	//console.log(data);
        	location.href = url;
        }
    });
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

	for (var key = lines.length, l_s0, l_s1; key--;)
	{
		l_s0 = lines[key].segments[0].point;
		l_s1 = lines[key].segments[1].point;
		l_s0.x = l_s0.x.toFixed(2)-0;
		l_s1.x = l_s1.x.toFixed(2)-0;
		l_s0.y = l_s0.y.toFixed(2)-0;
		l_s1.y = l_s1.y.toFixed(2)-0;
	}

	add_text_lines();
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
			}
			else if (rast2 > rast && view.zoom < 2.2)
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
			rast = Math.sqrt(Math.pow(event.touches[1].pageX - event.touches[0].pageX, 2) + Math.pow(event.touches[1].pageY - event.touches[0].pageY, 2));
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
		add_text_lines();
	}
}

function remove_none_shvy()
{
	var line;
	seam_lines = [];
	for (var i = lines.length; i--;)
	{
		lines[i].segments[0].point.x = +lines[i].segments[0].point.x.toFixed(2);
		lines[i].segments[0].point.y = +lines[i].segments[0].point.y.toFixed(2);
		lines[i].segments[1].point.x = +lines[i].segments[1].point.x.toFixed(2);
		lines[i].segments[1].point.y = +lines[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diags.length; i--;)
	{
		diags[i].segments[0].point.x = +diags[i].segments[0].point.x.toFixed(2);
		diags[i].segments[0].point.y = +diags[i].segments[0].point.y.toFixed(2);
		diags[i].segments[1].point.x = +diags[i].segments[1].point.x.toFixed(2);
		diags[i].segments[1].point.y = +diags[i].segments[1].point.y.toFixed(2);
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
}

function rotate_final()
{
	for (var i = project.activeLayer.children.length; i--;)
	{
		if (project.activeLayer.children[i].segments !== undefined)
		{
			project.activeLayer.children[i].rotate(-angle_rotate, view.center);
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

function zerkalo(p_u)
{
	var chertezh = new CompoundPath();

	chertezh.addChildren(lines);
	chertezh.addChildren(diags);
	for (var i = seam_lines.length; i--;)
	{
		chertezh.addChild(seam_lines[i]);
	}
	chertezh.scale(-1, 1);

	project.activeLayer.removeChildren();

	project.activeLayer.addChildren(lines);
	project.activeLayer.addChildren(diags);
	project.activeLayer.addChildren(text_points);
	for (var i = seam_lines.length; i--;)
	{
		project.activeLayer.addChild(seam_lines[i]);
	}

	chertezh.remove();

	for (var i = lines.length; i--;)
	{
		lines[i].segments[0].point.x = +lines[i].segments[0].point.x.toFixed(2);
		lines[i].segments[0].point.y = +lines[i].segments[0].point.y.toFixed(2);
		lines[i].segments[1].point.x = +lines[i].segments[1].point.x.toFixed(2);
		lines[i].segments[1].point.y = +lines[i].segments[1].point.y.toFixed(2);
	}
	for (var i = diags.length; i--;)
	{
		diags[i].segments[0].point.x = +diags[i].segments[0].point.x.toFixed(2);
		diags[i].segments[0].point.y = +diags[i].segments[0].point.y.toFixed(2);
		diags[i].segments[1].point.x = +diags[i].segments[1].point.x.toFixed(2);
		diags[i].segments[1].point.y = +diags[i].segments[1].point.y.toFixed(2);
	}
	okruglenie_all_segments();

	for (var key = 0; key < lines.length; key++)
    {
    	add_text_v_or_h(lines[key]);
    	if (p_u === 1)
    	{
    		text_lines[lines[key].data.id].content = (+text_lines[lines[key].data.id].content);
    	}
    	else
    	{
	    	text_lines[lines[key].data.id].content = (+text_lines[lines[key].data.id].content * p_u).toFixed(1);
	    }
    }

    for (var key = 0; key < diags.length; key++)
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
    for (var i = 0; i < lines.length; i++)
    {
    	if (i === 0)
    	{
    		j = lines.length - 1;
    	}
    	else
    	{
    		j = i - 1;
    	}
    	if (lines[i] !== lines[j] && obshaya_point(lines[i], lines[j]) !== null)
    	{
    		moveVertexName(lines[i], lines[j], obshaya_point(lines[i], lines[j]));
    	}
    }

}

});