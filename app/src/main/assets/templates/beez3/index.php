<?php
/**
* @version   $Id: index.php 15529 2013-11-13 22:04:39Z kevin $
 * @author RocketTheme http://www.rockettheme.com
 * @copyright Copyright (C) 2007 - 2016 RocketTheme, LLC
 * @license http://www.gnu.org/licenses/gpl-2.0.html GNU/GPLv2 only
 *
 * Gantry uses the Joomla Framework (http://www.joomla.org), a GNU/GPLv2 content management system
 *
 */
// no direct access
defined( '_JEXEC' ) or die( 'Restricted index access' );

// load and inititialize gantry class
require_once(dirname(__FILE__) . '/lib/gantry/gantry.php');
$gantry->init();

// get the current preset
$gpreset = str_replace(' ','',strtolower($gantry->get('name')));

?>
<!doctype html>
<html xml:lang="<?php echo $gantry->language; ?>" lang="<?php echo $gantry->language;?>" >
<head>
	<?php /*if ($gantry->get('layout-mode') == '960fixed') : ?>
	<meta name="viewport" content="width=960px, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<?php elseif ($gantry->get('layout-mode') == '1200fixed') : ?>
	<meta name="viewport" content="width=1200px, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<?php else : ?>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<?php endif; */?>
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<?php if ($gantry->browser->name == 'ie') : ?>
	<meta content="IE=edge" http-equiv="X-UA-Compatible" />
<?php endif; ?>	
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-touch-fullscreen" content="yes">
	<meta name="msapplication-tap-highlight" content="no">
	<link href="/templates/gantry/images/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
	<script src="/media/jui/js/jquery.min.js"></script>
	<script src="/media/jui/js/jquery-noconflict.js"></script>
	<script src="/media/jui/js/jquery-migrate.min.js"></script>
    <?php
        $gantry->displayHead();
        if ($gantry->browser->name == 'ie'){
        	if ($gantry->browser->shortversion == 9){
        		$gantry->addInlineScript("if (typeof RokMediaQueries !== 'undefined') window.addEvent('domready', function(){ RokMediaQueries._fireEvent(RokMediaQueries.getQuery()); });");
        	}
			if ($gantry->browser->shortversion == 8){
				$gantry->addScript('html5shim.js');
			}
		}
		if ($gantry->get('layout-mode', 'responsive') == 'responsive') $gantry->addScript('rokmediaqueries.js');
		if ($gantry->get('loadtransition')) {
		$gantry->addScript('load-transition.js');
		$hidden = ' class="rt-hidden"';}

    ?>
	<script src="/templates/gantry/chosen/chosen.jquery.min.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/jquery.json.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/jquery.maskedinput.min.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/jquery.noty.packaged.min.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/relax.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/jquery.scrollTo.min.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/jquery.validationEngine.js" type="text/javascript"></script>
	<script src="/templates/gantry/js/jquery.validationEngine-ru.js" type="text/javascript"></script>
	<link rel="stylesheet" href="/templates/gantry/font-awesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="/templates/gantry/chosen/chosen.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css" integrity="sha384-AysaV+vQoT3kOAXZkl02PThvDr8HYKPZhNT5h/CXfBThSRXQ6jW5DO2ekP5ViFdi" crossorigin="anonymous">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/js/bootstrap.min.js" integrity="sha384-BLiI7JTZm+JWlgKa0M0kGRpJbF2J8q+qreVrKBC47e3K6BW78kGLrCkeRX6I9RoK" crossorigin="anonymous"></script>
</head>
<body <?php echo $gantry->displayBodyTag(); ?>>
	<div id="sketch_editor" class="sketch_window" style="display: none;">
		<canvas data-paper-scope="1" height="770" width="1300" resize="true" id="myCanvas"></canvas>
		<label class="sketch_hud btn btn-success grid_check"><input id="useGrid" type="checkbox" class="check" checked="checked"><span></span></label>
		<button id="close_sketch" class="sketch_hud btn btn-success">Сохранить и закрыть</button>
		<button id ="createScreenshot" class="sketch_hud" style="display:none;">Сделать скриншот</button>
		<img id = 'screen' src="" alt="" class="sketch_hud">
		<div id="window" class="sketch_hud">
			<center>
				<input name="newLength" id="newLength" value="" placeholder="Введите длину" type="tel">
				<br>
				<input id = "ok" type="button" value="ОК">
				<input id = "close" type="button" value="Закрыть">
			</center>
		</div>
		<button class="sketch_hud btn btn-primary" id ="cancelLastAction" ><i class="fa fa-reply" aria-hidden="true"></i> Отменить шаг</button>
		<button class="sketch_hud btn btn-danger" id ="reset" ><i class="fa fa-undo" aria-hidden="true"></i> Начать сначала</button>
	</div>
	<div class="main_wrapper">
		<?php /** Begin Top Surround **/ if ($gantry->countModules('top') or $gantry->countModules('header')) : ?>
		<header id="rt-top-surround">
			<?php /** Begin Top **/ if ($gantry->countModules('top')) : ?>
			<div id="rt-top" <?php echo $gantry->displayClassesByTag('rt-top'); ?>>
				<div class="rt-container">
					<?php echo $gantry->displayModules('top','standard','standard'); ?>
					<div class="clear"></div>
				</div>
			</div>
			<?php /** End Top **/ endif; ?>
			<?php /** Begin Header **/ if ($gantry->countModules('header')) : ?>
			<div id="rt-header">
				<div class="rt-container">
					<?php echo $gantry->displayModules('header','standard','standard'); ?>
					<div class="clear"></div>
				</div>
			</div>
			<?php /** End Header **/ endif; ?>
		</header>
		<?php /** End Top Surround **/ endif; ?>
		<?php /** Begin Drawer **/ if ($gantry->countModules('drawer')) : ?>
		<div id="rt-drawer">
			<div class="rt-container">
				<?php echo $gantry->displayModules('drawer','standard','standard'); ?>
				<div class="clear"></div>
			</div>
		</div>
		<?php /** End Drawer **/ endif; ?>
		<?php /** Begin Showcase **/ if ($gantry->countModules('showcase')) : ?>
		<div id="rt-showcase">
			<div class="rt-showcase-pattern">
				<div class="rt-container">
					<?php echo $gantry->displayModules('showcase','standard','standard'); ?>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<?php /** End Showcase **/ endif; ?>
		<div id="rt-transition"<?php if ($gantry->get('loadtransition')) echo $hidden; ?>>
			<div id="rt-mainbody-surround">
				<?php /** Begin Feature **/ if ($gantry->countModules('feature')) : ?>
				<div id="rt-feature">
					<div class="rt-container">
						<?php echo $gantry->displayModules('feature','standard','standard'); ?>
						<div class="clear"></div>
					</div>
				</div>
				<?php /** End Feature **/ endif; ?>
				<?php /** Begin Utility **/ if ($gantry->countModules('utility')) : ?>
				<div id="rt-utility">
					<div class="rt-container">
						<?php echo $gantry->displayModules('utility','standard','standard'); ?>
						<div class="clear"></div>
					</div>
				</div>
				<?php /** End Utility **/ endif; ?>
				<?php /** Begin Breadcrumbs **/ if ($gantry->countModules('breadcrumb')) : ?>
				<div id="rt-breadcrumbs">
					<div class="rt-container">
						<?php echo $gantry->displayModules('breadcrumb','standard','standard'); ?>
						<div class="clear"></div>
					</div>
				</div>
				<?php /** End Breadcrumbs **/ endif; ?>
				<?php /** Begin Main Top **/ if ($gantry->countModules('maintop')) : ?>
				<div id="rt-maintop">
					<div class="rt-container">
						<?php echo $gantry->displayModules('maintop','standard','standard'); ?>
						<div class="clear"></div>
					</div>
				</div>
				<?php /** End Main Top **/ endif; ?>
				<?php /** Begin Full Width**/ if ($gantry->countModules('fullwidth')) : ?>
				<div id="rt-fullwidth">
					<?php echo $gantry->displayModules('fullwidth','basic','basic'); ?>
						<div class="clear"></div>
					</div>
				<?php /** End Full Width **/ endif; ?>
				<?php /** Begin Main Body **/ ?>
				<div class="container">
					<div class="row">
						<div class="col-xs-12 col-md-12 bd-content">
							<?php echo $gantry->displayMainbody('mainbody','sidebar','standard','standard','standard','standard','standard'); ?>
						</div>
					</div>
				</div>
				<?php /** End Main Body **/ ?>
				<?php /** Begin Main Bottom **/ if ($gantry->countModules('mainbottom')) : ?>
				<div id="rt-mainbottom">
					<div class="rt-container">
						<?php echo $gantry->displayModules('mainbottom','standard','standard'); ?>
						<div class="clear"></div>
					</div>
				</div>
				<?php /** End Main Bottom **/ endif; ?>
				<?php /** Begin Extension **/ if ($gantry->countModules('extension')) : ?>
				<div id="rt-extension">
					<div class="rt-container">
						<?php echo $gantry->displayModules('extension','standard','standard'); ?>
						<div class="clear"></div>
					</div>
				</div>
				<?php /** End Extension **/ endif; ?>
			</div>
		</div>
		<?php /** Begin Bottom **/ if ($gantry->countModules('bottom')) : ?>
		<div id="rt-bottom">
			<div class="rt-container">
				<?php echo $gantry->displayModules('bottom','standard','standard'); ?>
				<div class="clear"></div>
			</div>
		</div>
		<?php /** End Bottom **/ endif; ?>
		<?php /** Begin Footer Section **/ if ($gantry->countModules('footer') or $gantry->countModules('copyright')) : ?>
		<footer id="rt-footer-surround">
			<?php /** Begin Footer **/ if ($gantry->countModules('footer')) : ?>
			<div id="rt-footer">
				<div class="rt-container">
					<?php echo $gantry->displayModules('footer','standard','standard'); ?>
					<div class="clear"></div>
				</div>
			</div>
			<?php /** End Footer **/ endif; ?>
			<?php /** Begin Copyright **/ if ($gantry->countModules('copyright')) : ?>
			<div id="rt-copyright">
				<div class="rt-container">
					<?php echo $gantry->displayModules('copyright','standard','standard'); ?>
					<div class="clear"></div>
				</div>
			</div>
			<?php /** End Copyright **/ endif; ?>
		</footer>
		<?php /** End Footer Surround **/ endif; ?>
		<?php /** Begin Debug **/ if ($gantry->countModules('debug')) : ?>
		<div id="rt-debug">
			<div class="rt-container">
				<?php echo $gantry->displayModules('debug','standard','standard'); ?>
				<div class="clear"></div>
			</div>
		</div>
		<?php /** End Debug **/ endif; ?>
		<?php /** Begin Analytics **/ if ($gantry->countModules('analytics')) : ?>
		<?php echo $gantry->displayModules('analytics','basic','basic'); ?>
		<?php /** End Analytics **/ endif; ?>
	</div>
	<script>
		function getUrlParameter(sParam) {
			var sPageURL = decodeURIComponent(window.location.search.substring(1)),
				sURLVariables = sPageURL.split('&'),
				sParameterName,
				i;

			for (i = 0; i < sURLVariables.length; i++) {
				sParameterName = sURLVariables[i].split('=');

				if (sParameterName[0] === sParam) {
					return sParameterName[1] === undefined ? true : sParameterName[1];
				}
			}
		};
		
		jQuery( document ).ready(function(){
				
			//Автозамена запятой на точку
			jQuery( "#newLength" ).on("keyup",function(e){
				jQuery( this ).val( jQuery( this ).val().replace(',', '.') );
				var code = (e.keyCode ? e.keyCode : e.which);
				if (code==13) {
					jQuery("#ok").click();
				}
			});
				
			jQuery( ".section_header" ).click(function(){
				jQuery ( this ).next(".section_content").slideToggle();
			});
			
			jQuery( ".one-touch-view tbody tr td.one-touch" ).click(function(){
				var go_to = jQuery( this ).closest("tr").data("href");
				location.href = go_to;	
			});
			
			if(jQuery("div").is("#system-message")) {
				jQuery(".alert").each(function(){
					var alert_message = jQuery( this );
					alert_message.find(".alert-message").each(function(){
						var text_msg = jQuery( this ).text(),
							type_msg = "information",
							parent_message = jQuery( this ).parent("div").parent(".alert");
						
						if( parent_message.hasClass("alert-error") ) {
							type_msg = "error";
						}
						if( parent_message.hasClass("alert-notice") ) {
							type_msg = "information";
						}
						if( parent_message.hasClass("alert-success") ) {
							type_msg = "success";
						}
						if( parent_message.hasClass("alert-warning") ) {
							type_msg = "warning";
						}
						var n = noty({
							theme: 'relax',
							layout: 'center',
							maxVisible: 5,
							type: type_msg,
							text: text_msg
						});
					});
				});
				jQuery("#system-message-container").detach();
			}
		});
	</script>
	</body>
</html>
<?php
$gantry->finalize();
?>
