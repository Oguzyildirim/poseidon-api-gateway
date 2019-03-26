import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Ship from './ship';
import ShipDetail from './ship-detail';
import ShipUpdate from './ship-update';
import ShipDeleteDialog from './ship-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShipUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShipUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShipDetail} />
      <ErrorBoundaryRoute path={match.url} component={Ship} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShipDeleteDialog} />
  </>
);

export default Routes;
