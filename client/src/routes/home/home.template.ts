import { html } from '@genesislcap/web-core';
import type { Home } from './home';

export const HomeTemplate = html<Home>`
  <rapid-layout>
    <rapid-layout-region>
      <rapid-layout-item title="Accounts" closable>
        <!-- GRID -->
        <!-- Wrap the grid-pro-client-side-datasource in a rapid-grid-pro element -->
        <rapid-grid-pro
          persist-column-state-key="grid-pro-accounts-column-state"
          enable-row-flashing
          enable-cell-flashing
        >
          <!-- Add the grid pro client side component -->
          <!-- resource-name - This name should match your request reply name -->
          <!-- max-rows - Set how many rows to load, the server maps it onto your API's page size -->
          <!-- disable-polling - Request replies are polled every 5 seconds by default, this loads the data once instead -->
          <grid-pro-client-side-datasource
            resource-name="ACCOUNTS_API"
            row-id="ACCOUNT_NUMBER"
            max-rows="100"
            disable-polling
          ></grid-pro-client-side-datasource>
        </rapid-grid-pro>
      </rapid-layout-item>
    </rapid-layout-region>
  </rapid-layout>
`;
