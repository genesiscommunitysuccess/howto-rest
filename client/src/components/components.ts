import { EntityManagement } from '@genesislcap/foundation-entity-management';
import { Form } from '@genesislcap/foundation-forms';
import { foundationLayoutComponents } from '@genesislcap/foundation-layout';
import { getApp } from '@genesislcap/foundation-shell/app';
import { FoundationRouter } from '@genesislcap/foundation-ui';
import * as zeroDesignSystem from '@genesislcap/foundation-zero';
import { g2plotChartsComponents } from '@genesislcap/g2plot-chart';
import { NotPermittedComponent } from './not-permitted-component';
// GRID:
// Import the relevant modules
import { ModuleRegistry } from '@ag-grid-community/core';
import * as rapidDesignSystem from '@genesislcap/rapid-design-system';
import { rapidGridComponents } from '@genesislcap/rapid-grid-pro';
import { ServerSideRowModelModule } from '@ag-grid-enterprise/server-side-row-model';

// GRID:
// Register the AG Grid module in the AG grid module registry
ModuleRegistry.registerModules([ServerSideRowModelModule]);
/**
 * Ensure tree shaking doesn't remove these.
 */
FoundationRouter;
EntityManagement;
Form;
NotPermittedComponent;

/**
 * registerComponents.
 * @public
 */
export async function registerComponents() {
  const { configure: configureHeader } = await import('@genesislcap/foundation-header/config');
  /**
   * Register any PBC components with the design system
   */
  getApp().registerComponents({
    designSystem: rapidDesignSystem,
  });

  // GRID:
  rapidDesignSystem
    .provideDesignSystem()
    .register(
      rapidDesignSystem.baseComponents, // Register the rapidDesignSystem base components
      rapidGridComponents, // Register the rapid grid components
      g2plotChartsComponents,
      foundationLayoutComponents,
      [ServerSideRowModelModule], // Also register the AG grid module in the design system
    );

  configureHeader({
    templateOptions: {
      icon: 'rapid-icon',
      button: 'rapid-button',
      connectionIndicator: 'rapid-connection-indicator',
      select: 'rapid-select',
      option: 'rapid-option',
      flyout: 'rapid-flyout',
    },
  });

  /**
   * Still required while we transition all PBCs to rapid. Remove when complete.
   */
  zeroDesignSystem
    .provideDesignSystem()
    .register(zeroDesignSystem.baseComponents, g2plotChartsComponents, foundationLayoutComponents);
}
