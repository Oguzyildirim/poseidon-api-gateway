import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProductCategory } from 'app/shared/model/product-category.model';
import { getEntities as getProductCategories } from 'app/entities/product-category/product-category.reducer';
import { getEntity, updateEntity, createEntity, reset } from './product.reducer';
import { IProduct } from 'app/shared/model/product.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProductUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IProductUpdateState {
  isNew: boolean;
  productCategoryId: string;
}

export class ProductUpdate extends React.Component<IProductUpdateProps, IProductUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      productCategoryId: '0',
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

    this.props.getProductCategories();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { productEntity } = this.props;
      const entity = {
        ...productEntity,
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
    this.props.history.push('/entity/product');
  };

  render() {
    const { productEntity, productCategories, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="poseidonApiGatewayApp.product.home.createOrEditLabel">
              <Translate contentKey="poseidonApiGatewayApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : productEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="product-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="levelOneLabel" for="levelOne">
                    <Translate contentKey="poseidonApiGatewayApp.product.levelOne">Level One</Translate>
                  </Label>
                  <AvField id="product-levelOne" type="text" name="levelOne" />
                </AvGroup>
                <AvGroup>
                  <Label id="levelTwoLabel" for="levelTwo">
                    <Translate contentKey="poseidonApiGatewayApp.product.levelTwo">Level Two</Translate>
                  </Label>
                  <AvField id="product-levelTwo" type="text" name="levelTwo" />
                </AvGroup>
                <AvGroup>
                  <Label id="levelThreeLabel" for="levelThree">
                    <Translate contentKey="poseidonApiGatewayApp.product.levelThree">Level Three</Translate>
                  </Label>
                  <AvField id="product-levelThree" type="text" name="levelThree" />
                </AvGroup>
                <AvGroup>
                  <Label id="partNoLabel" for="partNo">
                    <Translate contentKey="poseidonApiGatewayApp.product.partNo">Part No</Translate>
                  </Label>
                  <AvField id="product-partNo" type="text" name="partNo" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    <Translate contentKey="poseidonApiGatewayApp.product.description">Description</Translate>
                  </Label>
                  <AvField id="product-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="uomLabel" for="uom">
                    <Translate contentKey="poseidonApiGatewayApp.product.uom">Uom</Translate>
                  </Label>
                  <AvField id="product-uom" type="text" name="uom" />
                </AvGroup>
                <AvGroup>
                  <Label id="mtmlUomLabel" for="mtmlUom">
                    <Translate contentKey="poseidonApiGatewayApp.product.mtmlUom">Mtml Uom</Translate>
                  </Label>
                  <AvField id="product-mtmlUom" type="text" name="mtmlUom" />
                </AvGroup>
                <AvGroup>
                  <Label id="explanationLabel" for="explanation">
                    <Translate contentKey="poseidonApiGatewayApp.product.explanation">Explanation</Translate>
                  </Label>
                  <AvField id="product-explanation" type="text" name="explanation" />
                </AvGroup>
                <AvGroup>
                  <Label id="pictureLabel" for="picture">
                    <Translate contentKey="poseidonApiGatewayApp.product.picture">Picture</Translate>
                  </Label>
                  <AvField id="product-picture" type="text" name="picture" />
                </AvGroup>
                <AvGroup>
                  <Label id="informationLabel" for="information">
                    <Translate contentKey="poseidonApiGatewayApp.product.information">Information</Translate>
                  </Label>
                  <AvField id="product-information" type="text" name="information" />
                </AvGroup>
                <AvGroup>
                  <Label for="productCategory.levelOne">
                    <Translate contentKey="poseidonApiGatewayApp.product.productCategory">Product Category</Translate>
                  </Label>
                  <AvInput id="product-productCategory" type="select" className="form-control" name="productCategoryId">
                    <option value="" key="0" />
                    {productCategories
                      ? productCategories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.levelOne}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/product" replace color="info">
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
  productCategories: storeState.productCategory.entities,
  productEntity: storeState.product.entity,
  loading: storeState.product.loading,
  updating: storeState.product.updating,
  updateSuccess: storeState.product.updateSuccess
});

const mapDispatchToProps = {
  getProductCategories,
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
)(ProductUpdate);
