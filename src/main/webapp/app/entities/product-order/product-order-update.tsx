import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShip } from 'app/shared/model/ship.model';
import { getEntities as getShips } from 'app/entities/ship/ship.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product-order.reducer';
import { IProductOrder } from 'app/shared/model/product-order.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProductOrderUpdateState {
  isNew: boolean;
  shipId: string;
}

export class ProductOrderUpdate extends React.Component<IProductOrderUpdateProps, IProductOrderUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      shipId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getShips();
  }

  saveEntity = (event, errors, values) => {
    values.placedDate = convertDateTimeToServer(values.placedDate);

    if (errors.length === 0) {
      const { productOrderEntity } = this.props;
      const entity = {
        ...productOrderEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/product-order');
  };

  render() {
    const { productOrderEntity, ships, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="apigatewayApp.productOrder.home.createOrEditLabel">
              <Translate contentKey="apigatewayApp.productOrder.home.createOrEditLabel">Create or edit a ProductOrder</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : productOrderEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="product-order-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="placedDateLabel" for="placedDate">
                    <Translate contentKey="apigatewayApp.productOrder.placedDate">Placed Date</Translate>
                  </Label>
                  <AvInput
                    id="product-order-placedDate"
                    type="datetime-local"
                    className="form-control"
                    name="placedDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.productOrderEntity.placedDate)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel">
                    <Translate contentKey="apigatewayApp.productOrder.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="product-order-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && productOrderEntity.status) || 'COMPLETED'}
                  >
                    <option value="COMPLETED">
                      <Translate contentKey="apigatewayApp.OrderStatus.COMPLETED" />
                    </option>
                    <option value="PENDING">
                      <Translate contentKey="apigatewayApp.OrderStatus.PENDING" />
                    </option>
                    <option value="CANCELLED">
                      <Translate contentKey="apigatewayApp.OrderStatus.CANCELLED" />
                    </option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="codeLabel" for="code">
                    <Translate contentKey="apigatewayApp.productOrder.code">Code</Translate>
                  </Label>
                  <AvField
                    id="product-order-code"
                    type="text"
                    name="code"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="ship.email">
                    <Translate contentKey="apigatewayApp.productOrder.ship">Ship</Translate>
                  </Label>
                  <AvInput
                    id="product-order-ship"
                    type="select"
                    className="form-control"
                    name="ship.id"
                    value={isNew ? ships[0] && ships[0].id : productOrderEntity.ship.id}
                  >
                    {ships
                      ? ships.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.email}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/product-order" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  ships: storeState.ship.entities,
  productOrderEntity: storeState.productOrder.entity,
  loading: storeState.productOrder.loading,
  updating: storeState.productOrder.updating,
  updateSuccess: storeState.productOrder.updateSuccess
});

const mapDispatchToProps = {
  getShips,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductOrderUpdate);
