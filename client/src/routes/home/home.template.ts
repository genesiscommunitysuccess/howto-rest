import { html } from '@genesislcap/web-core';
import type { Home } from './home';

export const HomeTemplate = html<Home>`
  <zero-layout>
    <zero-layout-region type="horizontal">
      <zero-layout-region>
        <zero-layout-item title="Infinite Scrolling" closable>
          <!-- GRID -->
          <!-- Wrap the grid-pro-server-side-datasource in a rapid-grid-pro element -->
          <!-- Make sure the persist-column-state-key is set to the ssrm (server side row model) column state -->
          <rapid-grid-pro
            persist-column-state-key="grid-pro-ssrm-column-state"
            enable-row-flashing
            enable-cell-flashing
          >
            <!-- Add the grid pro server side component -->
            <!-- resource-name - This name should match your request reply name -->
            <!-- max-rows - Set your desired max rows for the grid -->
            <!-- pagination - Enables pagination, each page with display max-rows number of rows -->
            <!-- zero-based-view-number - If your API's page number starts from zero, add this attribute -->
            <!-- This grid has infinite scrolling -->
            <grid-pro-server-side-datasource
              resource-name="ACCOUNTS_API"
              row-id="ACCOUNT_NUMBER"
              max-rows="30"
              zero-based-view-number
            ></grid-pro-server-side-datasource>
          </rapid-grid-pro>
        </zero-layout-item>
      </zero-layout-region>
      <zero-layout-region>
        <zero-layout-item title="Pagination" closable>
          <rapid-grid-pro
            persist-column-state-key="grid-pro-ssrm-column-state"
            enable-row-flashing
            enable-cell-flashing
          >
            <!-- This grid uses pagination -->
            <grid-pro-server-side-datasource
              resource-name="ACCOUNTS_API"
              row-id="ACCOUNT_NUMBER"
              max-rows="25"
              pagination
              zero-based-view-number
            ></grid-pro-server-side-datasource>
          </rapid-grid-pro>
        </zero-layout-item>
      </zero-layout-region>
    </zero-layout-region>
  </zero-layout>
`;
