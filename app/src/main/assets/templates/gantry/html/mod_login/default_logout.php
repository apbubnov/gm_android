<?php
/**
 * @package     Joomla.Site
 * @subpackage  mod_login
 *
 * @copyright   Copyright (C) 2005 - 2016 Open Source Matters, Inc. All rights reserved.
 * @license     GNU General Public License version 2 or later; see LICENSE.txt
 */

defined('_JEXEC') or die;

JHtml::_('behavior.keepalive');
?>
<div class="container">
	<div class="row">
		<div class="col-xs-6 col-md-6 bd-content">
			<a href="/index.php?option=com_gm_ceiling&task=mainpage"><img src="/images/gm-logo.png" style="width: 170px;" alt="Гильдия Мастеров"></a>
		</div>
		<div class="col-xs-6 col-md-6 bd-content">
			<div class="top_login">
				<form action="<?php echo JRoute::_(htmlspecialchars(JUri::getInstance()->toString(), ENT_COMPAT, 'UTF-8'), true, $params->get('usesecure')); ?>" method="post" id="login-form" class="form-vertical">
				<?php if ($params->get('greeting')) : ?>
					<div class="login-greeting">
					<?php if ($params->get('name') == 0) : ?>
						<?php echo JText::sprintf('MOD_LOGIN_HINAME', htmlspecialchars($user->get('name'), ENT_COMPAT, 'UTF-8')); ?>
					<?php else : ?>
						<?php echo JText::sprintf('MOD_LOGIN_HINAME', htmlspecialchars($user->get('username'), ENT_COMPAT, 'UTF-8')); ?>
					<?php endif; ?>
					</div>
				<?php endif; ?>
					<div class="logout-button">
						<input type="submit" name="Submit" class="btn btn-primary" value="<?php echo JText::_('JLOGOUT'); ?>" />
						<input type="hidden" name="option" value="com_users" />
						<input type="hidden" name="task" value="user.logout" />
						<input type="hidden" name="return" value="<?php echo $return; ?>" />
						<?php echo JHtml::_('form.token'); ?>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>