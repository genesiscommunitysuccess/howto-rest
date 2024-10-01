import { html } from '@genesislcap/web-core';
import type { Pipelines } from './pipelines';

export const PipelinesTemplate = html<Pipelines>`
  <zero-layout>
    <zero-layout-region type="horizontal">
      <zero-layout-region>
        <zero-layout-item title="Trade Accounts View" closable>
          <rapid-grid-pro
            persist-column-state-key="grid-pro-ssrm-column-state"
            enable-row-flashing
            enable-cell-flashing
          >
            <grid-pro-server-side-datasource
              resource-name="TRADE_ACCOUNTS_VIEW"
              row-id="TRADE_ID"
              max-rows="30"
            ></grid-pro-server-side-datasource>
          </rapid-grid-pro>
        </zero-layout-item>
      </zero-layout-region>
        <zero-layout-region>
          <zero-layout-item title="Trades" closable>
            <rapid-grid-pro
              persist-column-state-key="grid-pro-ssrm-column-state"
              enable-row-flashing
              enable-cell-flashing
            >
              <grid-pro-server-side-datasource
                resource-name="ALL_TRADES"
                row-id="TRADE_ID"
                max-rows="30"
              ></grid-pro-server-side-datasource>
            </rapid-grid-pro>
          </zero-layout-item>
        </zero-layout-region>
    </zero-layout-region>
  </zero-layout>
`;
